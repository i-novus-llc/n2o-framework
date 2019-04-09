import React from "react";
import PropTypes from "prop-types";
import {
  ListGroup,
  ListGroupItem,
  ListGroupItemHeading,
  ListGroupItemText
} from "reactstrap";
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
