import React from "react";
import PropTypes from "prop-types";
import { CustomInput, Input, Button } from "reactstrap";
import { isBoolean } from "lodash";

class ToDoForm extends React.Component {
  state = {
    text: "",
    priority: false
  };

  handleChange = fieldId => {
    return e => {
      this.setState({
        [fieldId]: isBoolean(e.target.checked)
          ? e.target.checked
          : e.target.value
      });
    };
  };

  handleAdd = () => {
    this.props.onAdd({ text: this.state.text, priority: this.state.priority });
    this.setState({
      text: "",
      priority: false
    });
  };

  render() {
    return (
      <div className="todo-form">
        <Input
          type="textarea"
          name="text"
          value={this.state.text}
          onChange={this.handleChange("text")}
        />
        <CustomInput
          type="checkbox"
          id="checkbox-add"
          label="Приоритетная"
          value={this.state.priority}
          onChange={this.handleChange("priority")}
        />
        <Button onClick={this.handleAdd}>Добавить</Button>
      </div>
    );
  }
}

ToDoForm.propTypes = {};

ToDoForm.defaultProps = {};

export default ToDoForm;
