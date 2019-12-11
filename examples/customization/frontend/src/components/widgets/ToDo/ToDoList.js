import React from "react";
import PropTypes from "prop-types";
import ListGroup from "reactstrap/lib/ListGroup";
import { map } from "lodash";

import ToDoItem from "./ToDoItem";

class ToDoList extends React.Component {
  render() {
    const { data, onChange } = this.props;
    return (
      <ListGroup className="todo-list">
        {map(data, item => (
          <ToDoItem {...item} onChange={onChange} />
        ))}
      </ListGroup>
    );
  }
}

ToDoList.propTypes = {
  data: PropTypes.array
};

ToDoList.defaultProps = {};

export default ToDoList;
