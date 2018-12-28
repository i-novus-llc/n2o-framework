import React from 'react';
import PropTypes from 'prop-types';
import { ButtonDropdown, DropdownToggle, DropdownMenu } from 'reactstrap';

/**
 * Дропдаун
 * @reactProps {string} id
 * @reactProps {node} children - элкменты меню дропдауна
 * @reactProps {string} color - цвет кнопки дропдауна
 * @reactProps {string} title - заголовок кнопки
 */
class Dropdown extends React.Component {
  constructor(props) {
    super(props);

    this.toggle = this.toggle.bind(this);
    this.state = {
      dropdownOpen: false
    };
  }

  /**
   * смена видимости меню дропдауна
   */
  toggle(e) {
    this.setState({
      dropdownOpen: !this.state.dropdownOpen
    });
  }

  /**
   * Базовый рендер
   */
  render() {
    const { color, title, children } = this.props;
    return (
      <ButtonDropdown isOpen={this.state.dropdownOpen} toggle={this.toggle}>
        <DropdownToggle caret color={color} id={this.props.id}>
          {title}
        </DropdownToggle>
        <DropdownMenu>{children}</DropdownMenu>
      </ButtonDropdown>
    );
  }
}

Dropdown.propTypes = {
  color: PropTypes.string,
  title: PropTypes.node,
  id: PropTypes.string,
  children: PropTypes.node
};

export default Dropdown;
