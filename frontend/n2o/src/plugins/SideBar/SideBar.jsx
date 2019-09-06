import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';
import { isEqual, map, get } from 'lodash';
import SidebarItemContainer from './SidebarItemContainer';
import UserBox from '../../components/snippets/UserBox/UserBox';
import {
  compose,
  withState,
  lifecycle,
  withHandlers,
  setDisplayName,
} from 'recompose';

/**
 * Sidebar
 * @param activeId - id активного элемента
 * @param brand - текст бренда
 * @param brandImage - картинка бренда
 * @param userBox - настройка userBox
 * @param items - массив итемов
 * @param visible - видимость
 * @param width - длина сайдбара
 * @param controlled - флаг контроллед режима
 * @param onToggle - переключение compressed
 * @param extra - екстра итемы
 * @returns {*}
 * @constructor
 */
export function SideBar({
  activeId,
  brand,
  brandImage,
  userBox,
  items,
  visible,
  width,
  controlled,
  onToggle,
  extra,
}) {
  const renderItems = items =>
    map(items, (item, i) => (
      <SidebarItemContainer
        key={i}
        item={item}
        activeId={activeId}
        sidebarOpen={visible}
      />
    ));

  const withoutBrandImage = !visible && !brandImage;

  return (
    <aside
      className={cn('n2o-sidebar', { 'n2o-sidebar--compressed': !visible })}
    >
      <div className="n2o-sidebar__nav-brand n2o-nav-brand d-flex justify-content-center">
        <a className="d-flex align-items-center" href="/">
          {brandImage && (
            <img
              className={cn({ 'mr-2': visible })}
              src={brandImage}
              alt=""
              width="30"
              height="30"
            />
          )}
          {(visible || withoutBrandImage) && (
            <span className="n2o-nav-brand__text">
              {withoutBrandImage ? brand.substring(0, 1) : brand}
            </span>
          )}
        </a>
      </div>
      {userBox && (
        <UserBox {...userBox} compressed={!visible}>
          {renderItems(get(userBox, 'items'))}
        </UserBox>
      )}
      <nav className="n2o-sidebar__nav">
        <ul className="n2o-sidebar__nav-list">{renderItems(items)}</ul>
      </nav>
      <div className="n2o-sidebar__footer">
        <div className="n2o-sidebar__extra">
          <ul className="n2o-sidebar__nav-list">{renderItems(extra)}</ul>
        </div>
        {!controlled && (
          <div onClick={onToggle} className="n2o-sidebar__toggler">
            <span className="n2o-sidebar__nav-item">
              <span
                className={cn('n2o-sidebar__nav-item-icon', {
                  'mr-1': visible,
                })}
              >
                <i className="fa fa-angle-double-left" />
              </span>
              {visible && <span>Скрыть</span>}
            </span>
          </div>
        )}
      </div>
    </aside>
  );
}

SideBar.propTypes = {
  /**
   * ID активного элемента
   */
  activeId: PropTypes.string,
  /**
   * Бренд сайдбара
   */
  brand: PropTypes.string,
  /**
   * Картинка бренда
   */
  brandImage: PropTypes.string,
  /**
   * Блок пользователя
   */
  userBox: PropTypes.object,
  /**
   * Элементы сайдбара
   */
  items: PropTypes.array,
  /**
   * Флаг сжатия
   */
  visible: PropTypes.bool,
  /**
   * Длина
   */
  width: PropTypes.number,
  /**
   * Флаг включения режима controlled
   */
  controlled: PropTypes.bool,
  /**
   * Callback на переключение сжатия
   */
  onToggle: PropTypes.func,
  /**
   * Extra элементы
   */
  extra: PropTypes.array,
};

SideBar.defaultProps = {
  controlled: false,
};

export default compose(
  setDisplayName('Sidebar'),
  withState('visible', 'setVisible', ({ visible }) => visible),
  withHandlers({
    onToggle: ({ visible, setVisible }) => () => setVisible(!visible),
  }),
  lifecycle({
    componentDidUpdate(prevProps) {
      if (!isEqual(prevProps.visible, this.props.visible)) {
        this.setState({ visible: this.props.visible });
      }
    },
  })
)(SideBar);
