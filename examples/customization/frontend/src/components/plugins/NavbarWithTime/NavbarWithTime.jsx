import React from "react";
import PropTypes from "prop-types";
import Navbar from "reactstrap/lib/Navbar";
import Nav from "reactstrap/lib/Nav";
import NavItem from "reactstrap/lib/NavItem";
import NavbarBrand from "reactstrap/lib/NavbarBrand";
import NavbarToggler from "reactstrap/lib/NavbarToggler";
import Collapse from "reactstrap/lib/Collapse";
import NavLink from "reactstrap/lib/NavLink";
import UncontrolledDropdown from "reactstrap/lib/UncontrolledDropdown";
import DropdownToggle from "reactstrap/lib/DropdownToggle";
import DropdownMenu from "reactstrap/lib/DropdownMenu";
import DropdownItem from "reactstrap/lib/DropdownItem";
import { isEmpty } from "lodash";
import moment from "moment";
import { NavLink as RouterLink } from "react-router-dom";

import NavbarBrandContent from "n2o/lib/plugins/Header/SimpleHeader/NavbarBrandContent";
import NavItemContainer from "n2o/lib/plugins/Header/SimpleHeader/NavItemContainer";

/**
 *
 */
class NavbarWithTime extends React.Component {
  state = {
    time: null
  };

  componentDidMount() {
    clearTimeout(this.timeout);
    this.setState(
      () => ({ time: moment().format(this.props.timeFormat) }),
      () => {
        this.timeout = setInterval(() => {
          this.setState(() => ({
            time: moment().format(this.props.timeFormat)
          }));
        }, 1000);
      }
    );
  }

  render() {
    const { items, brandImage, brand } = this.props;
    const { time } = this.state;
    const mapItems = (items, options) =>
      items.map((item, i) => {
        if (item.type === "dropdown") {
          return (
            <UncontrolledDropdown nav inNavbar>
              <DropdownToggle nav caret>
                {item.label}
              </DropdownToggle>
              <DropdownMenu left>
                {item.subItems.map((subItem, i) => (
                  <DropdownItem>
                    {subItem.linkType === "outer" ? (
                      <NavLink href={subItem.href}>{subItem.label}</NavLink>
                    ) : (
                      <RouterLink
                        exact
                        className="nav-link"
                        to={subItem.href}
                        activeClassName="active"
                      >
                        {subItem.label}
                      </RouterLink>
                    )}
                  </DropdownItem>
                ))}
              </DropdownMenu>
            </UncontrolledDropdown>
          );
        } else {
          return (
            <NavItem>
              {item.linkType === "outer" ? (
                <NavLink href={item.href}>{item.label}</NavLink>
              ) : (
                <RouterLink
                  exact
                  className="nav-link"
                  to={item.href}
                  activeClassName="active"
                >
                  {item.label}
                </RouterLink>
              )}
            </NavItem>
          );
        }
      });

    const navItems = mapItems(items);

    return (
      <Navbar color="info" expand="md" dark>
        <NavbarBrand href="/">
          <NavbarBrandContent brand={brand} brandImage={brandImage} />
        </NavbarBrand>
        <NavbarToggler onClick={this.toggle} />
        <Collapse isOpen={this.state.isOpen} navbar>
          <Nav navbar>{navItems}</Nav>
          <Nav className="ml-auto" navbar>
            <NavItem>
              <NavLink><strong>{time}</strong></NavLink>
            </NavItem>
          </Nav>
        </Collapse>
      </Navbar>
    );
  }
}

NavbarWithTime.propTypes = {
  timeFormat: PropTypes.string,
  brand: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
  brandImage: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
  items: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      label: PropTypes.string.isRequired,
      href: PropTypes.string,
      linkType: PropTypes.oneOf(["inner", "outer"]),
      type: PropTypes.oneOf(["dropdown", "link", "text"]).isRequired,
      subItems: PropTypes.array
    })
  )
};

NavbarWithTime.defaultProps = {
  items: [],
  timeFormat: 'HH:mm:ss'
};

export default NavbarWithTime;
