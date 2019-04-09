import React from "react";
import PropTypes from "prop-types";
import {
  Navbar,
  Nav,
  NavItem,
  NavbarBrand,
  NavbarToggler,
  Collapse
} from "reactstrap";
import { isEmpty } from "lodash";
import moment from "moment";

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
      items.map((item, i) => (
        <NavItemContainer key={i} item={item} options={options} />
      ));
    const navItems = mapItems(items);

    return (
      <Navbar color="dark" expand="md" dark>
        <NavbarBrand href="/">
          <NavbarBrandContent brand={brand} brandImage={brandImage} />
        </NavbarBrand>
        <NavbarToggler onClick={this.toggle} />
        <Collapse isOpen={this.state.isOpen} navbar>
          <Nav navbar>{navItems}</Nav>
          <Nav className="ml-auto" navbar>
            <NavItem>
              <span className="nav-link">{time}</span>
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
  items: []
};

export default NavbarWithTime;
