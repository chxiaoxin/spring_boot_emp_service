import React, { Component } from 'react';
import {Jumbotron} from 'react-bootstrap'
import Main from './container/Main';
import Upload from './container/Upload';

class App extends Component {

  constructor(props) {
		super(props);
		this.state = {employees: []};
  }
  
	componentDidMount() {
    var rest = require('rest')
    var mime = require('rest/interceptor/mime')
    var client = rest.wrap(mime)
		client({method: 'GET', path: '/api/employees'}).done(response => {
			this.setState({employees: response.entity._embedded.employees});
		});
	}
	
  render() {
    return (
      <div className="App">
        <div>
          <Jumbotron>
            <h1>EMP Info Management Service</h1>
            <p>
              A rudimentary web app that provides CSV data uploading and error records downloading services
            </p>
          </Jumbotron>
          <Upload />
        </div>
        <Main employees={this.state.employees}/>
      </div>
    );
  }
}

export default App;
