import React from 'react';
import PropTypes from 'prop-types';
import { Popover, PopoverHeader, PopoverBody } from 'reactstrap';
import { id } from '../../../utils/id';
import cx from 'classnames';

/**
 * Popover
 * @reacProps {string} header - заголовок Popover
 * @reacProps {string} body - тело Popover
 * @reacProps {string} placement - позиция Popover
 */

class N2OPopover extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showPopover: false,
    };
    this.fieldId = id();
    this.onToggle = this.onToggle.bind(this);
  }

  onToggle() {
    this.setState({
      showPopover: !this.state.showPopover,
    });
  }

  render() {
    const {
      header,
      body,
      placement,
      children,
      className,
      help,
      icon,
    } = this.props;
    return (
      <div className={cx('n2o-popover', className)}>
        <div id={this.fieldId} onClick={this.onToggle}>
          {help && <i className="fa fa-question-circle" />}
          {icon && <i className={icon} />}
          {children}
        </div>
        <Popover
          placement={placement}
          isOpen={this.state.showPopover}
          target={this.fieldId}
          toggle={this.onToggle}
        >
          {help ? (
            <div dangerouslySetInnerHTML={{ __html: help }} />
          ) : (
            <React.Fragment>
              {header && <PopoverHeader>{header}</PopoverHeader>}
              {body && <PopoverBody>{body}</PopoverBody>}
            </React.Fragment>
          )}
        </Popover>
      </div>
    );
  }
}

N2OPopover.propTypes = {
  header: PropTypes.string,
  body: PropTypes.oneOfType([PropTypes.string, PropTypes.object]),
  placement: PropTypes.string,
  icon: PropTypes.string,
  help: PropTypes.string,
};

N2OPopover.defaultProps = {
  header: 'header',
  body: 'body',
  placement: 'right',
  icon: '',
};

export default N2OPopover;
