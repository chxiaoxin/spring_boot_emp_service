import React from 'react';
import {FormGroup, FormControl, Button, Label} from 'react-bootstrap'

class Upload extends React.Component {

    constructor(props) {
		super(props);
        this.state = {files: [], selected: ''};
        this.onClick = this.onClick.bind(this)
    }
    
    componentDidMount() {
        var rest = require('rest')
        var mime = require('rest/interceptor/mime')
        var client = rest.wrap(mime)
            client({method: 'GET', path: '/files'}).done(response => {
                this.setState({files: response.entity});
            });
        }

    buildOptions(options) {
        var optionArray = [<option id={'empty'} key={-1} value={""}>{}</option>]
        for (var i = 0; i < options.length; i++){
            optionArray.push(<option id={i} key={i} value={options[i]}>{options[i]}</option>)
        }
        return optionArray
    }

    onClick() {
        setTimeout(() => {
            const response = {
              file: this.state.selected,
            };
            window.location.href = response.file;
          }, 100);
    }

    render() {

        return (
            <div>
                
                <form method="POST" encType="multipart/form-data" action="/" >
                    <label htmlFor='xFile'>Click Here and Select File to Upload</label>
                    <FormControl id="xFile" type="file" name="file" style={{position:"absolute", clip:"rect(0 0 0 0)"}}/>
                    <Button type="submit" value="Upload" bsStyle="default" bsSize="xsmall" style={{margin:"10px"}}>submit</Button>
                    
                    <FormGroup>
                        <FormControl onChange={(e) => this.setState({selected:e.target.value})} componentClass="select" placeholder="select" style={{display:'inline', width:'50%'}} >
                            {this.buildOptions(this.state.files)}
                        </FormControl>
                        <Button  value="Download" bsStyle="success" style={{margin:'10px'}} onClick={this.onClick} >Download File</Button>
                    </FormGroup>
                </form>
            </div>
        );
    }
}

export default Upload;