import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { createStructuredSelector } from "reselect";
import { Badge } from 'reactstrap';
import cx from "classnames";

import { getCounter } from "./selectors";

export class Counter extends Component {
  getCount() {
    const {
      count,
      noNumber,
      showZero,
      overflowCount,
      overflowText
    } = this.props;

    if (noNumber) {
      return null;
    }

    if (count === 0 && showZero) {
      return 0;
    }

    return count > overflowCount
      ? overflowText
        ? overflowText
        : `${overflowCount}+`
      : count || null;
  }

  render() {
    const { color, noNumber, children } = this.props;
    const count = this.getCount();
    return (
      <span className={cx("n2o-counter")}>
        {children}
        <sup className={cx("n2o-counter-number", { "no-number": noNumber })}>
          <Badge tag="div" color={color}>
            {count}
          </Badge>
        </sup>
      </span>
    );
  }
}

Counter.propTypes = {
  bind: PropTypes.string,
  count: PropTypes.string,
  color: PropTypes.oneOf([
    "primary",
    "secondary",
    "success",
    "danger",
    "warning",
    "info",
    "light",
    "dark"
  ]),
  noNumber: PropTypes.bool,
  showZero: PropTypes.bool,
  overflowCount: PropTypes.number,
  overflowText: PropTypes.string
};

Counter.defaultProps = {
  color: "danger",
  noNumber: false,
  showZero: false,
  overflowCount: 99
};

const mapStateToProps = createStructuredSelector({
  count: (state, props) => getCounter(props.bind || "all")(state)
});

function mapDispatchToProps(dispatch, ownProps) {
  return {
    dispatch
  };
}
export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Counter);
