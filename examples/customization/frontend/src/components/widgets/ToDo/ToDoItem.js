import React from "react";
import PropTypes from "prop-types";
import ListGroupItem from "reactstrap/lib/ListGroupItem";
import CustomInput from "reactstrap/lib/CustomInput";
import cx from "classnames";

class ToDoItem extends React.Component {
  handleChange = () => {
    const { id, done, onChange } = this.props;
    onChange(id, !done);
  };

  render() {
    const { id, text, done, priority } = this.props;
    return (
      <ListGroupItem
        className={cx("todo-item", {
          "todo-item_priority": priority,
          "todo-item_done": done
        })}
      >
        <CustomInput
          type="checkbox"
          id={`checkbox-${id}`}
          inline
          checked={done}
          label={text}
          onChange={this.handleChange}
        />
        {priority ? <span className="text-danger">Приоритетная</span> : null}
      </ListGroupItem>
    );
  }
}

ToDoItem.propTypes = {
  text: PropTypes.string,
  done: PropTypes.bool,
  priority: PropTypes.bool
};

ToDoItem.defaultProps = {
  done: false,
  priority: false
};

export default ToDoItem;
