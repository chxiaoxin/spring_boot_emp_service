import React from 'react'
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table'


var columnMap = {
	'department': '^[\\w-_\\*]+$',
	'designation': '^Developer$|^Senior Developer$|^Manager$|^Team Lead$|^VP$|^CEO$',
	'salary': '^\\d+$',
	'joinDate': '^\\d{4}-\\d{2}-\\d{2}$'
  }

class Main extends React.Component {

  zip(keys, values) {
    var mapper = {}
    for(var i = 0; i < keys.length; i++) {
        mapper[keys[i]] = values[i]
    }
    return mapper
  }
	
  onBeforeSaveCell(row, cellName, cellValue) {
	var regex = new RegExp(columnMap[cellName])
	if (!regex.test(cellValue)) return false
  }

  onAfterSaveCell(row, cellName, cellValue) {
    var rest = require('rest')
    var mime = require('rest/interceptor/mime')
	  var client = rest.wrap(mime)
	  client({method: 'PUT', path: '/update?updateColumn=' + cellName + '&name=' + row.name + '&updateValue=' + cellValue}).done(response => {
		  console.log(response.entity)
	  })
  }

  render() {
    var valid_employees = this.props.employees.filter((e) => e['valid'] == true)
    const data = valid_employees.map(employee => this.zip(Object.keys(employee).slice(0,5), Object.values(employee).slice(0,5)));
    const cellEditProp = {
      mode: 'click',
      blurToSave: true,
      beforeSaveCell: this.onBeforeSaveCell, 
      afterSaveCell: this.onAfterSaveCell 
    };
    return (
      <div>
        <BootstrapTable
          data={ data }
          keyField='name'
          cellEdit={ cellEditProp }
          pagination>
          <TableHeaderColumn dataField='name'>Name</TableHeaderColumn>
          <TableHeaderColumn dataField='department'>Department</TableHeaderColumn>
          <TableHeaderColumn dataField='designation'>Designation</TableHeaderColumn>
          <TableHeaderColumn dataField='salary'>Salary</TableHeaderColumn>
          <TableHeaderColumn dataField='joinDate'>JoinDate</TableHeaderColumn>
        </BootstrapTable>
      </div>
    );
  }
}

export default Main;
