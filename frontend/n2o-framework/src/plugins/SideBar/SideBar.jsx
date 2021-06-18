import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import isEqual from 'lodash/isEqual'
import map from 'lodash/map'
import get from 'lodash/get'
import {
    compose,
    withState,
    lifecycle,
    withHandlers,
    setDisplayName,
} from 'recompose'
import { withTranslation } from 'react-i18next'

import UserBox from '../../components/snippets/UserBox/UserBox'

import { SidebarItemContainer } from './SidebarItemContainer'

/**
 * Sidebar
 * @param activeId - id активного элемента
 * @param brand - текст бренда
 * @param brandImage - картинка бренда
 * @param userBox - настройка userBox
 * @param items - массив итемов
 * @param visible - видимость
 * @param sidebarOpen - видимость если controlled
 * @param width - длина сайдбара
 * @param controlled - флаг контроллед режима
 * @param onToggle - переключение compressed
 * @param extra - екстра итемы
 * @param homePageUrl - url брэнда
 * @param className - class
 * @param t - функция перевода
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
    sidebarOpen,
    controlled,
    onToggle,
    extra,
    homePageUrl,
    className,
    t,
}) {
    const currentVisible = controlled ? sidebarOpen : visible

    const renderItems = items => map(items, (item, i) => (
        <SidebarItemContainer
            key={i}
            item={item}
            activeId={activeId}
            sidebarOpen={currentVisible}
        />
    ))

    const withoutBrandImage = !currentVisible && !brandImage

    return (
        <aside
            className={classNames('n2o-sidebar', className, { 'n2o-sidebar--compressed': !currentVisible })}
        >
            <div className="n2o-sidebar__nav-brand n2o-nav-brand">
                <a className="d-flex align-items-center ml-1" href={homePageUrl}>
                    {brandImage && (
                        <img
                            className={classNames({ 'mr-2': currentVisible })}
                            src={brandImage}
                            alt=""
                            width="30"
                            height="30"
                        />
                    )}
                    {(currentVisible || withoutBrandImage) && (
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
                                className={classNames('n2o-sidebar__nav-item-icon', {
                                    'mr-1': visible,
                                })}
                            >
                                <i className="fa fa-angle-double-left" />
                            </span>
                            {visible && <span>{t('hide')}</span>}
                        </span>
                    </div>
                )}
            </div>
        </aside>
    )
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
     * Флаг сжатия если controlled
     */
    sidebarOpen: PropTypes.bool,
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
    /**
     * Адрес ссылка бренда
     */
    homePageUrl: PropTypes.string,
    className: PropTypes.string,
    t: PropTypes.func,
}

SideBar.defaultProps = {
    controlled: false,
    brand: '',
    homePageUrl: '/',
    t: () => {},
}

export default compose(
    withTranslation(),
    setDisplayName('Sidebar'),
    withState('visible', 'setVisible', ({ visible }) => visible),
    withHandlers({
        onToggle: ({ visible, setVisible }) => () => setVisible(!visible),
    }),
    lifecycle({
        componentDidUpdate(prevProps) {
            if (!isEqual(prevProps.visible, this.props.visible)) {
                this.setState({ visible: this.props.visible })
            }
        },
    }),
)(SideBar)
