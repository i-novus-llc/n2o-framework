import React, { useEffect, useRef, useState } from 'react'
import DrawerRC from 'rc-drawer'
import PropTypes from 'prop-types'
import classNames from 'classnames'

/**
 * Drawer
 * @reactProps {string} className - className компонента
 * @reactProps {boolean} visible - состояние Drawer
 * @reactProps {boolean} backdropClosable - закрытие Drawer по клику на backdrop
 * @reactProps {boolean} closable - отобразить/скрыть крестик
 * @reactProps {boolean} keyboard - разрешить/запретить закрытие Drawer по клавише Esc
 * @reactProps {string} placement - позиция Drawer
 * @reactProps {function} onClose - функция закрытия по клику
 * @reactProps {function} onHandleClick - функция закрытия по клику на крестик
 * @reactProps {boolean} backdrop - вкл/выкл подложку
 * @reactProps {Element} children - дочерний элемент DOM
 * @reactProps {boolean} animation - вкл/выкл анимацию
 * @reactProps {string|number} width - ширина Drawer
 * @reactProps {string|number} height - высота Drawer
 * @reactProps {string|node} title - заголовок
 * @reactProps {string|node} footer - "подвал"
 * @reactProps {string|node} children - основная часть контента
 * @reactProps {boolean} fixedFooter - флаг фиксирования футера
 * @example
 */

function Drawer(props) {
    const {
        className,
        backdropClosable,
        visible,
        placement,
        onClose,
        onHandleClick,
        backdrop,
        level,
        animation,
        width,
        height,
        title,
        footer,
        fixedFooter,
        children,
        keyboard,
        closable,
    } = props

    const [paddingBottom, setPaddingBottom] = useState(0)
    const footerRef = useRef(null)

    useEffect(() => {
        if (footerRef.current) {
            setPaddingBottom(footerRef.current.clientHeight)
        }
    }, [visible, setPaddingBottom])

    return (
        <DrawerRC
            className={classNames('n2o-drawer n2o-snippet', className, {
                'without-close-button': closable === false,
                'drawer-animation': animation,
            })}
            open={visible}
            width={width}
            height={height}
            placement={placement}
            onClose={onClose}
            showMask={backdrop}
            level={level}
            maskClosable={backdropClosable}
            onHandleClick={onHandleClick}
            keyboard={keyboard}
        >
            <div
                className="n2o-drawer-content-wrapper"
                style={fixedFooter ? { paddingBottom } : {}}
            >
                <div className="n2o-drawer-children-wrapper">
                    {title && <div className="drawer-title">{title}</div>}
                    <div className="drawer-children">{children}</div>
                    <div
                        className={classNames('drawer-footer', {
                            'drawer-footer--fixed': fixedFooter,
                        })}
                        ref={footerRef}
                    >
                        {footer}
                    </div>
                </div>
            </div>
        </DrawerRC>
    )
}

Drawer.propTypes = {
    className: PropTypes.string,
    /**
   * Включение кнопки закрытия
   */
    closable: PropTypes.bool,
    /**
   * Включение закрытия по клику на маску(backdrop)
   */
    backdropClosable: PropTypes.bool,
    /**
   * Видимость модального окна
   */
    visible: PropTypes.bool,
    /**
   * разрешить/запретить закрытие Drawer по клавише Esc
   */
    keyboard: PropTypes.bool,
    /**
   * Позиция компонента
   */
    placement: PropTypes.string,
    onClose: PropTypes.func,
    onHandleClick: PropTypes.func,
    /**
   * Включение маски(backdrop)
   */
    backdrop: PropTypes.bool,
    /**
   * Включение анимации
   */
    animation: PropTypes.bool,
    /**
   * Сдвиг элемента или группы элементов при открытии (level={'all'} для сдвига всех эл-в)
   */
    level: PropTypes.oneOfType([PropTypes.string, PropTypes.array]),
    /**
   * Ширина компонента
   */
    width: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    /**
   * Высота компонента
   */
    height: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    /**
   * Заголовок компонента
   */
    title: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
    /**
   * Нижняя часть компонента
   */
    footer: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
    /**
   * Основная часть компонента
   */
    children: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
    /**
   * Флаг фиксирования футера
   */
    fixedFooter: PropTypes.bool,
}

Drawer.defaultProps = {
    animation: true,
    backdropClosable: true,
    level: false,
    fixedFooter: false,
    closable: true,
    keyboard: true,
}

export default Drawer
