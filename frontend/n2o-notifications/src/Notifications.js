import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { createStructuredSelector } from "reselect";
import { map } from "lodash";
import { Toast, ToastBody, ToastHeader } from 'reactstrap';
import cx from "classnames";

import { stackSelector } from "./selectors";

class Notifications extends Component {
  renderToasts() {
    const { stack = [] } = this.props;
    return map(stack, item => (
      <Toast>
        <ToastHeader icon={item.icon} toggle={() => {}}>{item.title}</ToastHeader>
        <ToastBody>{item.body}</ToastBody>
      </Toast>
    ));
  }

  render() {
    const { position } = this.props;
    return (
      <div className={cx("n2o-notification", position)}>
        {this.renderToasts()}
      </div>
    );
  }
}

Notifications.propTypes = {
  position: PropTypes.oneOf([
    "top-left",
    "top-center",
    "top-right",
    "bottom-left",
    "bottom-center",
    "bottom-right"
  ]),
  order: PropTypes.oneOf(["top-down", "down-up"]),
  pauseOnHover: PropTypes.bool,
  stack: PropTypes.array
};

Notifications.defaultProps = {
  position: "top-right",
  order: "top-down",
  pauseOnHover: false,
  stack: []
};

const mapStateToProps = createStructuredSelector({
  stack: stackSelector
});

function mapDispatchToProps(dispatch, ownProps) {
  return {
    dispatch,
  };
}
export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Notifications);
