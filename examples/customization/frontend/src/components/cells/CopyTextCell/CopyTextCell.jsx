import React, { Component } from "react";
import PropTypes from "prop-types";
import { get } from "lodash";
import { CopyToClipboard } from "react-copy-to-clipboard";
import Icon from "n2o-framework/lib/components/snippets/Icon/Icon";
import Text from "n2o-framework/lib/components/snippets/Text/Text";
import "./CopyText.scss";

class CopyTextCell extends Component {
  state = {
    copied: false
  };

  handleCopy = () => {
    this.setState(
      () => ({
        copied: true
      }),
      () => {
        setTimeout(() => {
          this.setState(() => ({
            copied: false
          }));
        }, 2000);
      }
    );
  };

  render() {
    const { model, fieldKey, id, visible, ...rest } = this.props;
    const { copied } = this.state;
    const text = model && get(model, fieldKey || id);
    return visible ? (
      <span>
        <Text text={text} {...rest} />
        <CopyToClipboard text={text} onCopy={this.handleCopy}>
          <span
            className={
              copied
                ? "n2o-icon n2o-copy-icon fa fa-check text-success"
                : "n2o-icon n2o-copy-icon fa fa-clipboard"
            }
          />
        </CopyToClipboard>
      </span>
    ) : null;
  }
}

CopyTextCell.propTypes = {
  model: PropTypes.object,
  fieldKey: PropTypes.string,
  className: PropTypes.string,
  format: PropTypes.string,
  visible: PropTypes.bool
};
CopyTextCell.defaultProps = {
  visible: true
};

export default CopyTextCell;
