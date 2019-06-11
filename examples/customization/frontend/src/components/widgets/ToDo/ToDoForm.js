import React from "react";
import PropTypes from "prop-types";
import { CustomInput, Input, Button } from "reactstrap";
import { isBoolean } from "lodash";

class ToDoForm extends React.Component {
  state = {
    text: "",
    priority: false
  };

  handleChangeText = e => {
    this.setState({
      text: e.target.value
    });
  };

  handleChangePriority = e => {
    this.setState({
      priority: e.target.checked
    });
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
          onChange={this.handleChangeText}
        />
        <CustomInput
          type="checkbox"
          id="checkbox-add"
          label="Приоритетная"
          checked={this.state.priority}
          onChange={this.handleChangePriority}
        />
        <Button color="primary" onClick={this.handleAdd}>
          Добавить
        </Button>
      </div>
    );
  }
}

ToDoForm.propTypes = {};

ToDoForm.defaultProps = {};

export default ToDoForm;
