import React from 'react';
import cn from 'classnames';
import { isEqual, map, get } from 'lodash';
import SidebarItemContainer from './SidebarItemContainer';
import UserBox from '../../components/snippets/UserBox/UserBox';
import { compose, withState, lifecycle, withHandlers } from 'recompose';

function SideBar({
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
  const mapLabel = (item, visible) => {
    return visible
      ? {
          ...item,
          oldLabel: item.label,
          label: (
            <div className="n2o-sidebar__item-content">
              <span className="n2o-sidebar__item-content-icon">
                <i className={cn(item.iconClass)} />
              </span>
              <span> {item.label}</span>
            </div>
          ),
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
          ),
        };
  };

  const mapLinkToDropdown = (item, visible) => {
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
              label: item.label,
            },
          ],
        };
  };

  const renderItems = items =>
    map(items, (item, i) => (
      <SidebarItemContainer
        key={i}
        item={mapLinkToDropdown(item, visible)}
        activeId={activeId}
        sidebarOpen={visible}
      />
    ));

  const withoutBrandImage = !visible && !brandImage;

  return (
    <aside
      className={cn('n2o-sidebar', { 'n2o-sidebar--compressed': !visible })}
      style={{ width: visible ? width : 60 }}
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

export default compose(
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
