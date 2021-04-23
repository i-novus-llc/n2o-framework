/**
 * Created by emamoshin on 01.06.2017.
 */
import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import find from 'lodash/find'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import filter from 'lodash/filter'
import first from 'lodash/first'
import get from 'lodash/get'

import TabNav from './TabNav'
import TabNavItem from './TabNavItem'
import TabContent from './TabContent'

/**
 * Компонент контейнера табов
 * @reactProps {string} className - css-класс
 * @reactProps {string} navClassName - css-класс для нава
 * @reactProps {string} maxHeight - кастом max-высота контента таба, фиксация табов
 * @reactProps {string} title - title табов
 * @reactProps {function} onChangeActive
 * @reactProps {function} hideSingleTab - скрывать / не скрывать навигацию таба, если он единственный
 * @reactProps {node} children - элемент потомок компонента Tabs
 * @reactProps {boolean} scrollbar - спрятать scrollbar (default = true)
 * @example
 * <Tabs>
 * {
 *   containers.map((cnt) =>
 *     <Tab id={cnt.id}
 *          title={cnt.name || cnt.id}
 *          active={cnt.opened}>
 *       <WidgetFactory containerId={cnt.id} pageId={cnt.pageId} fetchOnInit={cnt.fetchOnInit} {...cnt.widget} />
 *     </Tab>
 *   )
 * }
 * </Tabs>
 */

class Tabs extends React.Component {
    componentDidUpdate(prevProps, prevState, snapshot) {
        const { onChangeActive, children, activeId } = this.props

        const getActiveEntityVisibility = (children) => {
            const activeEntityMeta = first(
                filter(children, child => get(child, 'props.id') === activeId),
            )

            return get(activeEntityMeta, 'props.visible')
        }

        const activeEntityVisibilityChanged =
      getActiveEntityVisibility(children) !==
      getActiveEntityVisibility(prevProps.children)

        const firstVisibleTab = first(
            filter(children, child => child.props.visible),
        )

        if (activeEntityVisibilityChanged && !getActiveEntityVisibility(children)) {
            onChangeActive(get(firstVisibleTab, 'key'), prevProps.activeId)
        }
    }

  /**
   * установка активного таба
   * @param event
   * @param id
   * @param prevId
   */
  handleChangeActive = (event, id, prevId) => {
      this.props.onChangeActive(event, id, prevId)
  };

  /**
   * getter для айдишника активного таба
   * @return {Array|*}
   */
  get defaultOpenedId() {
      const { children, activeId } = this.props

      if (activeId) {
          return activeId
      }

      const foundChild = find(React.Children.toArray(children), child => child.props.active)
      return foundChild && foundChild.props.id
  }

  /**
   * Базовый рендер
   * @return {JSX.Element}
   */
  render() {
      const {
          className,
          navClassName,
          children,
          hideSingleTab,
          dependencyVisible,
          scrollbar,
          maxHeight,
          title,
      } = this.props

      const activeId = this.defaultOpenedId

      const tabContentStyle = maxHeight ? { maxHeight } : {}

      const tabNavItems = React.Children.map(children, (child) => {
          const { id, title, icon, disabled, visible } = child.props

          const hasSingleVisibleTab =
        children.filter(child => child.props.visible).length === 1

          if (
              (hasSingleVisibleTab && hideSingleTab) ||
        !dependencyVisible ||
        !visible
          ) {
              return null
          }

          return (
              <TabNavItem
                  id={id}
                  title={title}
                  icon={icon}
                  disabled={disabled}
                  active={activeId === id}
                  onClick={this.handleChangeActive}
              />
          )
      })

      return (
          <div
              className={classNames('n2o-nav-tabs-container', {
                  [className]: className,
                  fixed: maxHeight,
              })}
          >
              {!isEmpty(tabNavItems) && (
                  <div
                      className={classNames('n2o-nav-tabs', {
                          'n2o-nav-tabs_tabs-fixed': maxHeight,
                      })}
                  >
                      {title && <h5 className="n2o-nav-tabs__title">{title}</h5>}
                      <TabNav
                          className={classNames('n2o-nav-tabs__tabs-list', {
                              navClassName,
                          })}
                      >
                          {tabNavItems}
                      </TabNav>
                  </div>
              )}
              <div
                  className={classNames('n2o-tab-content__container', {
                      visible: dependencyVisible,
                      fixed: maxHeight,
                  })}
              >
                  <TabContent
                      className={classNames({
                          'tab-content_fixed': maxHeight,
                          'tab-content_fixed tabs-with-title':
                (title && maxHeight) || (title && maxHeight),
                          'tab-content_height-fixed': maxHeight,
                          'tab-content_no-scrollbar': scrollbar === false,
                      })}
                      style={tabContentStyle}
                  >
                      {React.Children.map(children, child => React.cloneElement(child, {
                          active: activeId === child.props.id,
                      }))}
                  </TabContent>
              </div>
          </div>
      )
  }
}

Tabs.propTypes = {
    /**
   * Класс
   */
    className: PropTypes.string,
    /**
   * Класс навигации
   */
    navClassName: PropTypes.string,
    /**
   * Callback на изменение активного таба
   */
    onChangeActive: PropTypes.func,
    children: PropTypes.node,
    /**
   * спрятать/не прятать scrollbar
   */
    scrollbar: PropTypes.bool,
    /**
   * кастомная max-высота контента. фиксация табов
   */
    height: PropTypes.string,
    /**
   * title табов
   */
    title: PropTypes.string,
}

Tabs.defaultProps = {
    onChangeActive: () => {},
    scrollbar: false,
}

export default Tabs
