import React, { Component } from "react";
import Button from "./button";

class Buttons extends Component {
  // the state part is executed only ONCE
  // when the current component is created
  state = {
    buttons: [
      { id: 1, value: 30 },
      { id: 2, value: 0 },
      { id: 3, value: 0 },
      { id: 4, value: 0 },
    ],
  };

  handleDelete = (id) => {
    console.log(id);
    const newButtons = this.state.buttons.filter((btn) => btn.id !== id);
    this.setState({ buttons: newButtons });
  };

  handleIncrement = (button) => {
    const newButtons = [...this.state.buttons];
    const index = newButtons.indexOf(button);
    newButtons[index] = { ...button };
    newButtons[index].value++;
    this.setState({ buttons: newButtons });
  };

  handleReset = () => {
    // console.log(id);
    const newButtons = this.state.buttons.map((btn) => {
      btn.value = 0;
      return btn;
    });
    this.setState({ buttons: newButtons });
  };

  render() {
    return (
      <div>
        <button className="btn btn-info mb-10" onClick={this.handleReset}>
          RESET
        </button>
        {this.state.buttons.map((btn) => (
          <Button
            key={btn.id}
            // value={btn.value}
            button={btn}
            // id={btn.id}
            onIncrement={this.handleIncrement}
            onDelete={this.handleDelete}
          >
            <h1>{btn.id}</h1>
          </Button>
        ))}
      </div>
    );
  }
}

export default Buttons;
