import React from 'react';
import Drawer from 'rc-drawer';
import PropTypes from 'prop-types';
import { Button } from 'reactstrap';

/**
 * Drawer
 * @reactProps {string} className -
 * @reactProps {string} wrapperClassName -
 * @reactProps {boolean} open -
 * @reactProps {string|number} width -
 * @reactProps {string|number} height -
 * @reactProps {string} placement -
 * @reactProps {boolean} animation -
 * @reactProps {function} onClose - вызывается при закрытии
 * @reactProps {Element} children - дочерний элемент DOM
 * @example
 */

class N2ODrawer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      open: false,
    };

    this.onSwitch = this.onSwitch.bind(this);
    this.getContainer = this.getContainer.bind(this);
  }
  onSwitch() {
    this.setState({
      open: !this.state.open,
    });
  }
  getContainer() {
    return this.container;
  }
  render() {
    const {
      className,
      wrapperClassName,
      title,
      footer,
      width,
      height,
      placement,
      animation,
      onClose,
      children,
    } = this.props;
    return (
      <React.Fragment>
        <Drawer
          wrapperClassName={wrapperClassName}
          open={this.state.open}
          width={width}
          height={height}
          placement={placement}
          duration={animation ? '.3s' : '.0s'}
          onClose={onClose}
          showMask={true}
          level={null}
        >
          <div>{title}</div>
          {children}
          <div>{footer}</div>
        </Drawer>
        <div className="d-flex justify-content-center">
          <Button onClick={this.onSwitch} style={{ zIndex: 10000 }}>
            {!this.state.open ? 'open' : 'close'}
          </Button>
        </div>
      </React.Fragment>
    );
  }
}

export default N2ODrawer;
