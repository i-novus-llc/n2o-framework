import React from "react";
import PropTypes from "prop-types";
import { map, isBoolean, sortBy, isEqual } from "lodash";

import { id } from "n2o/lib/utils/id";

import ToDoList from "./ToDoList";
import ToDoForm from "./ToDoForm";

import "./ToDo.scss";

class ToDo extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      data: props.data || [],
      prevData: [],
      checked: {}
    };
  }

  static getDerivedStateFromProps(props, state) {
    if (!isEqual(props.data, state.prevData)) {
      return {
        data: props.data,
        prevData: props.data
      };
    }
    return null;
  }

  handleCheck = (id, value) => {
    this.setState({
      checked: {
        ...this.state.checked,
        [id]: value
      }
    });
  };

  handleAdd = data => {
    this.setState({
      data: [
        ...this.state.data,
        {
          id: id(),
          done: false,
          ...data
        }
      ]
    });
  };

  render() {
    const { checked, data } = this.state;

    const list = sortBy(
      map(data, item => {
        const ci = checked[item.id];
        return {
          ...item,
          done: isBoolean(ci) ? ci : item.done
        };
      }),
      [
        item => {
          return !item.priority;
        }
      ]
    );

    return (
      <div className="todo">
        <h4>Список заметок</h4>
        <ToDoList data={list} onChange={this.handleCheck} />
        <ToDoForm onAdd={this.handleAdd} />
      </div>
    );
  }
}

ToDo.propTypes = {
  data: PropTypes.array
};

ToDo.defaultProps = {
  data: []
};

export default ToDo;
