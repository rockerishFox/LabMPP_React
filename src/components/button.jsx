import React, { Component } from "react";
import "./button.css";

class Button extends Component {
  //  JSX = JavaScript XML for describing UI
  // state = {
  //   value: this.props.button.value,
  // };

  styles = {
    backgroundColor: "Green",
    width: 100,
    borderRadius: 20,
    height: 50,
    fontSize: 20,
    color: "white",
  };

  // constructor() {
  //   super();
  //   this.btnClicked = this.btnClicked.bind(this);
  // }

  // btnClicked = () => {
  //   // console.log("Ai apasat pe buton!");
  //   // console.log(this);

  //   this.setState({ value: this.state.value + 1 }); // metoda care spune ca am updatat state-ul DOM-ului virtual
  //   // the method tells React that the state of the current component will change
  //   // React schedules a call to render() in the future
  //   // it will create a new tree, it will compare it to the old tree, and then it will update de real DOM
  //   // it will update only the changed element (Very efficient)
  // };

  handleParameter = (id) => {
    console.log(id);
  };

  render() {
    // console.log(this.props);
    return (
      <div>
        {/* {this.props.children} */}

        <button
          onClick={() => this.props.onIncrement(this.props.button)}
          // style={this.styles}
          className="btn btn-warning btn-md btn-number m-top"
        >
          {this.props.button.value}
        </button>
        {/* <button
          onClick={() => {
            this.handleParameter("hello");
          }}
          style={this.styles}
        >
          Hello
        </button> */}
        <button
          onClick={() => this.props.onDelete(this.props.button.id)}
          className="btn ml-1 btn-danger btn-md m-top"
        >
          DELETE
        </button>
      </div>
    );
  }
}

export default Button;
