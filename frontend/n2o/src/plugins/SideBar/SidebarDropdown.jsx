import React from 'react';
import { NavItem, Nav, NavLink } from 'reactstrap';
import cx from 'classnames';

import history from '../../history';

class SidebarDropdown extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      open: this.checkIfActive() || props.open,
    };

    this.onClick = this.onClick.bind(this);
  }

  checkIfActive() {
    return React.Children.toArray(this.props.children).some(child => {
      return child.props.to === history.location.pathname;
    });
  }

  componentWillReceiveProps(props) {
    this.setState({
      open: this.checkIfActive() || props.open,
    });
  }

  onClick(e) {
    e.preventDefault();
    this.setState({ open: !this.state.open });
  }

  render() {
    const { children, activeId, title } = this.props;
    const caretStyle = {
      marginLeft: 5,
    };

    return (
      <React.Fragment>
        <NavItem onClick={this.onClick}>
          <NavLink href="#">
            {title}
            <i
              className={cx('fa', {
                'fa-caret-up': this.state.open,
                'fa-caret-down': !this.state.open,
              })}
              style={caretStyle}
              aria-hidden="true"
            />
          </NavLink>
        </NavItem>
        {this.state.open && (
          <div className="n2o-sidebar-dropdown">
            <Nav pills>{children}</Nav>
          </div>
        )}
      </React.Fragment>
    );
  }
}

export default SidebarDropdown;
