import React from 'react';
import {
  Navbar,
  Nav,
  NavbarBrand,
  NavbarToggler,
  Collapse,
  Input,
  InputGroup,
  InputGroupAddon
} from 'reactstrap';

import NavbarBrandContent from '../Header/SimpleHeader/NavbarBrandContent';
import NavItemContainer from '../Header/SimpleHeader/NavItemContainer';

class SideBar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      visible: props.visible,
      isOpen: false
    };
    this.onClick = this.onClick.bind(this);
    this.toggle = this.toggle.bind(this);
  }

  componentWillReceiveProps(props) {
    if (props.visible || props.visible === false) {
      this.setState({ visible: props.visible });
    }
  }

  toggle() {
    this.setState({
      isOpen: !this.state.isOpen
    });
  }

  onClick() {
    this.setState({ visible: !this.state.visible });
  }

  mapLabel(item, visible) {
    return visible
      ? {
          ...item,
          oldLabel: item.label,
          label: (
            <span>
              {item.iconClass && (
                <span>
                  <i className={item.iconClass} />
                </span>
              )}
              <span> {item.label}</span>
            </span>
          )
        }
      : {
          ...item,
          center: true,
          oldLabel: item.label,
          label: (
            <span>
              {item.iconClass && (
                <span>
                  <i className={item.iconClass} />
                </span>
              )}
            </span>
          )
        };
  }

  mapLinkToDropdown(item, visible) {
    return visible || item.type !== 'link'
      ? item
      : {
          ...item,
          type: 'dropdown',
          subItems: [
            {
              id: item.id,
              type: 'link',
              href: item.href,
              label: item.label
            }
          ]
        };
  }

  render() {
    const {
      brand,
      brandImage,
      items,
      fixed,
      color,
      className,
      style,
      search,
      activeId,
      collapse
    } = this.props;

    const { visible } = this.state;
    const navItems = items.map((item, i) => (
      <NavItemContainer
        key={i}
        item={this.mapLabel(this.mapLinkToDropdown(item, visible), visible)}
        activeId={activeId}
        type="sidebar"
        sidebarOpen={visible}
      />
    ));

    const isInversed = color === 'inverse';
    const navColor = isInversed ? 'dark' : 'light';

    return (
      <div
        className={`n2o-sidebar-container n2o-sidebar-container-${color} sidebar-container-${
          fixed ? 'fixed' : 'relative'
        } ${this.state.visible ? 'open' : 'closed'}`}
        ref={sidebar => (this.sidebar = sidebar)}
      >
        <Navbar color={navColor} dark={isInversed} light={!isInversed} expand="md">
          <NavbarToggler onClick={this.toggle} />
          <NavbarBrand>
            <NavbarBrandContent brand={visible && brand} brandImage={brandImage} />
          </NavbarBrand>
          <Collapse isOpen={this.state.isOpen} navbar>
            <Nav navbar>
              {search && visible && (
                <InputGroup>
                  <Input placeholder="Поиск" />
                  <InputGroupAddon addonType="append">
                    <span className="input-group-text">
                      <i className="fa fa-search" aria-hidden="true" />
                    </span>
                  </InputGroupAddon>
                </InputGroup>
              )}
              {navItems}
              {collapse && !fixed && (
                <button className="n2o-sidebar-toggler" onClick={this.onClick}>
                  {this.state.visible ? (
                    <i className="fa fa-angle-left" aria-hidden="true" />
                  ) : (
                    <i className="fa fa-angle-right" aria-hidden="true" />
                  )}
                </button>
              )}
            </Nav>
          </Collapse>
        </Navbar>
        {collapse && fixed && (
          <button className="fixed-toggler n2o-sidebar-toggler" onClick={this.onClick}>
            {this.state.visible ? (
              <i class="fa fa-angle-left" aria-hidden="true" />
            ) : (
              <i class="fa fa-angle-right" aria-hidden="true" />
            )}
          </button>
        )}
      </div>
    );
  }
}

SideBar.defaultProps = {
  color: 'default',
  visible: true,
  fixed: false,
  className: '',
  items: [],
  search: false,
  style: {},
  collapse: true
};

export default SideBar;
