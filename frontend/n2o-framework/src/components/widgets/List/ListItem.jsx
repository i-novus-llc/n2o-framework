import React, { useEffect, isValidElement } from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

/**
 * Компонент ListItem виджета ListWidget
 * @param {Node|Object} leftTop - секция картинки
 * @param {Node|Object} leftBottom - секция картинки
 * @param {Node|String} header - секция заголовка
 * @param {Node|String} subHeader - секция подзаголовка
 * @param {Node|String} body - секция тела
 * @param {Node|String} rightTop - секция справа сверху
 * @param {Node|String} rightBottom - секция справа снизу
 * @param {Node|String} extra - extra секция
 * @param {boolean} selected - флаг активности строки
 * @param {function} onClick - callback на клик по строке
 * @param {boolean} divider - разделить между строками
 * @param {object} style - стили
 * @param {object} hasSelect
 * @param measure
 * @returns {*}
 * @constructor
 */
function HeaderWrapper({ children, isValid }) {
    if (isValid) {
        return children
    }

    return (
        <h3 className="n2o-widget-list-item-header">
            {children}
        </h3>
    )
}

HeaderWrapper.propTypes = {
    children: PropTypes.node,
    isValid: PropTypes.bool,
}

function ListItem({
    leftTop,
    leftBottom,
    header,
    subHeader,
    body,
    rightTop,
    rightBottom,
    extra,
    selected,
    onClick,
    divider,
    style,
    hasSelect,
    measure = () => {},
}) {
    useEffect(() => {
        /* FIXME временный костыль до рефакторинга List widget.
            measure пересчитывает выстоту прокидывая style через CellMeasurer,
            style не может быть dependency useEffect уйдет в беск. цикл.
            Использование height не приносит результата, пересчет прерывается.
            Решение 1 рачсет с delay. При медленном рендере может несработать */
        setTimeout(() => measure(), 400)
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    const renderImage = (image) => {
        if (isValidElement(image)) {
            return image
        }

        return <img src={image.src} alt={image.alt || ''} {...image} />
    }

    return (
        <div
            onClick={onClick}
            style={style}
            className={classNames('n2o-widget-list-item', {
                'n2o-widget-list-item--active': hasSelect && selected,
                'n2o-widget-list-item--divider': divider,
            })}
        >
            <div className="n2o-widget-list-item-left-container">
                {leftTop && <div className="n2o-widget-list-item-left-top">{renderImage(leftTop)}</div>}
                {leftBottom && <div className="n2o-widget-list-item-left-bottom">{leftBottom}</div>}
            </div>
            <div className="n2o-widget-list-item-main-container">
                <div className="n2o-widget-list-item-header-row">
                    <HeaderWrapper isValid={isValidElement(header) || isValidElement(subHeader)}>
                        {header && <div className="n2o-widget-list-item-header">{header}</div>}
                        {subHeader && <div className="n2o-widget-list-item-subheader text-muted">{subHeader}</div>}
                    </HeaderWrapper>
                </div>
                {body && <div className="n2o-widget-list-item-body">{body}</div>}
            </div>
            <div className="n2o-widget-list-item-right-container">
                {rightTop && <div className="n2o-widget-list-item-right-top">{rightTop}</div>}
                {rightBottom && <div className="n2o-widget-list-item-right-bottom">{rightBottom}</div>}
            </div>
            <div className="n2o-widget-list-item-extra-container">
                {extra && <div className="n2o-widget-list-item-extra">{extra}</div>}
            </div>
        </div>
    )
}

ListItem.propTypes = {
    leftTop: PropTypes.oneOfType([PropTypes.node, PropTypes.object]),
    leftBottom: PropTypes.oneOfType([PropTypes.node, PropTypes.object]),
    header: PropTypes.oneOfType([PropTypes.node, PropTypes.string]),
    subHeader: PropTypes.oneOfType([PropTypes.node, PropTypes.string]),
    body: PropTypes.oneOfType([PropTypes.node, PropTypes.string]),
    rightTop: PropTypes.oneOfType([PropTypes.node, PropTypes.string]),
    rightBottom: PropTypes.oneOfType([PropTypes.node, PropTypes.string]),
    extra: PropTypes.oneOfType([PropTypes.node, PropTypes.string]),
    selected: PropTypes.bool,
    onClick: PropTypes.func,
    divider: PropTypes.bool,
    style: PropTypes.object,
    hasSelect: PropTypes.bool,
    measure: PropTypes.func,
}

export default ListItem
