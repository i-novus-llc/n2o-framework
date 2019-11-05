import React from 'react';
import PropTypes from 'prop-types';
import Navbar from 'reactstrap/lib/Navbar';
import Nav from 'reactstrap/lib/Nav';
import NavItem from 'reactstrap/lib/NavItem';
import InputGroup from 'reactstrap/lib/InputGroup';
import InputGroupAddon from 'reactstrap/lib/InputGroupAddon';
import Input from 'reactstrap/lib/Input';
import NavbarBrand from 'reactstrap/lib/NavbarBrand';
import NavbarToggler from 'reactstrap/lib/NavbarToggler';
import Collapse from 'reactstrap/lib/Collapse';
import { isEmpty } from 'lodash';
import SecurityCheck from '../../../core/auth/SecurityCheck';

import NavbarBrandContent from './NavbarBrandContent';
import NavItemContainer from './NavItemContainer';

/**
 * Хедер-плагин
 * @param {Object} props - пропсы
 * @param {string|element} props.brand - брэнд
 * @param {string|element} props.brandImage - изображение брэнда
 * @param {array} props.items - элементы навбар-меню (левое меню)
 * @param {boolean} props.fixed - фиксированный хэдер или нет
 * @param {array} props.extraItems - элементы навбар-меню (правое меню)
 * @param {boolean} props.collapsed - находится в состоянии коллапса или нет
 * @param {boolean} props.search - есть поле поиска / нет поля поиска
 * @param {boolean} props.color - стиль хэдера (default или inverse)
 * @param {boolean} props.className - css-класс
 * @param {boolean} props.style - объект стиля
 * @example
 * //каждый item состоит из id {string}, label {string}, type {string} ('text', 'type' или 'dropdown'),
 * //href {string}(для ссылок), linkType {string}(для ссылок; значения - 'outer' или 'inner')
 * //subItems {array} (массив из элементов дропдауна)
 *<SimpleHeader  items = { [
 *     {
 *       id: 'link',
 *       label: 'link',
 *       href: '/test',
 *       type: 'link',
 *     },
 *     {
 *       id: 'dropdown',
 *       label: 'dropdown',
 *       type: 'dropdown',
 *       subItems: [{id: 'test1',label: 'test1', href: '/', badge: 'badge1', badgeColor: 'color1'},
 *       {id: 'test123', label: 'test1', href: '/',  badge: 'badge2', badgeColor: 'color2'}]
 *     },
 *     {
 *       id: 'test',
 *       label: 'test',
 *       type: 'dropdown',
 *       subItems: [{id: 'test123s',label: 'test1', href: '/', badge: 'badge1', badgeColor: 'color1'},
 *       {id: 'test12asd3',label: 'test1', href: '/',  badge: 'badge2', badgeColor: 'color2'}]
 *     }
 *     ] }
 *     extraItems = { [
 *     {
 *       id: "213",
 *       label: 'ГКБ №7',
 *       type: 'text',
 *     },
 *     {
 *       id: "2131",
 *       label: 'Постовая медсестра',
 *       type: 'dropdown',
 *       subItems: [{label: 'test1', href: '/', linkType: 'inner'}, {label: 'test1', href: '/'}]
 *     },
 *     {
 *       id: "2131",
 *       label: 'admin',
 *       type: 'dropdown',
 *       subItems: [{label: 'test1', href: '/'}, {label: 'test1', href: '/'}]
 *     }
 *     ] }
 *    brand="N2O"
 *    brandImage= "http://getbootstrap.com/assets/brand/bootstrap-solid.svg"
 *    activeId={"test123"}/>
 *
 */

class SimpleHeader extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isOpen: false,
    };

    this.toggle = this.toggle.bind(this);
  }

  toggle() {
    this.setState({
      isOpen: !this.state.isOpen,
    });
  }

  render() {
    const {
      color,
      fixed,
      items,
      activeId,
      extraItems,
      brandImage,
      brand,
      style,
      className,
      search,
    } = this.props;
    const isInversed = color === 'inverse';
    const navColor = isInversed ? 'primary' : 'light';
    const mapItems = (items, options) =>
      items.map((item, i) => (
        <NavItemContainer
          key={i}
          item={item}
          activeId={activeId}
          options={options}
        />
      ));

    const navItems = mapItems(items);
    const extraNavItems = mapItems(extraItems, { right: true });

    return (
      <div
        style={style}
        className={`navbar-container-${
          fixed ? 'fixed' : 'relative'
        } ${className} n2o-header n2o-header-${color} `}
      >
        <Navbar
          color={navColor}
          light={!isInversed}
          dark={isInversed}
          expand="md"
        >
          <NavbarBrand href="/">
            <NavbarBrandContent brand={brand} brandImage={brandImage} />
          </NavbarBrand>
          <NavbarToggler onClick={this.toggle} />
          <Collapse isOpen={this.state.isOpen} navbar>
            <Nav navbar>{navItems}</Nav>
            <Nav className="ml-auto" navbar>
              {extraNavItems}
              {search && (
                <NavItem>
                  <InputGroup>
                    <Input placeholder="Поиск" />
                    <InputGroupAddon addonType="append">
                      <span className="input-group-text">
                        <i className="fa fa-search" aria-hidden="true" />
                      </span>
                    </InputGroupAddon>
                  </InputGroup>
                </NavItem>
              )}
            </Nav>
          </Collapse>
        </Navbar>
      </div>
    );
  }
}

SimpleHeader.propTypes = {
  /**
   * ID активного элемента
   */
  activeId: PropTypes.string,
  /**
   * Бренд хедера
   */
  brand: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
  /**
   * Картинка бренда
   */
  brandImage: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
  /**
   * Элементы хедера
   */
  items: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      label: PropTypes.string.isRequired,
      href: PropTypes.string,
      linkType: PropTypes.oneOf(['inner', 'outer']),
      type: PropTypes.oneOf(['dropdown', 'link', 'text']).isRequired,
      subItems: PropTypes.array,
      badge: PropTypes.string,
      badgeColor: PropTypes.string,
    })
  ),
  /**
   * Extra элементы хедера
   */
  extraItems: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      label: PropTypes.string.isRequired,
      href: PropTypes.string,
      linkType: PropTypes.oneOf(['inner', 'outer']),
      type: PropTypes.oneOf(['dropdown', 'link', 'text']).isRequired,
      subItems: PropTypes.array,
      badge: 'badge',
      badgeColor: 'color',
    })
  ),
  /**
   * Строка поиска
   */
  search: PropTypes.bool,
  /**
   * Цвет хедера
   */
  color: PropTypes.oneOf(['inverse', 'default']),
  /**
   * Флаг фиксированного хедера
   */
  fixed: PropTypes.bool,
  /**
   * Флаг сжатости хедера
   */
  collapsed: PropTypes.bool,
  /**
   * Класс
   */
  className: PropTypes.string,
  /**
   * Стили
   */
  style: PropTypes.object,
};

SimpleHeader.defaultProps = {
  color: 'default',
  fixed: true,
  collapsed: true,
  className: '',
  items: [],
  extraItems: [],
  search: false,
  style: {},
};

export default SimpleHeader;
