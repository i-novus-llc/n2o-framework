import React from 'react';
import DrawerRC from 'rc-drawer';
import PropTypes from 'prop-types';
import cx from 'classnames';

/**
 * Drawer
 * @reactProps {string} className - className компонента
 * @reactProps {boolean} visible - состояние Drawer
 * @reactProps {boolean} backdropClosable - закрытие Drawer по клику на backdrop
 * @reactProps {boolean} closable - отобразить/скрыть крестик
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
 * @example
 */

function Drawer(props) {
  const {
    className,
    closable,
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
    children,
  } = props;
  return (
    <React.Fragment>
      <DrawerRC
        className={cx('n2o-drawer', animation && 'drawer-animation', className)}
        open={visible}
        width={width}
        height={height}
        placement={placement}
        onClose={onClose}
        showMask={backdrop}
        level={level}
        maskClosable={backdropClosable}
        onHandleClick={onHandleClick}
        handler={closable}
      >
        <div className="drawer-title">{title}</div>
        <div className="drawer-children">{children}</div>
        <div className="drawer-footer">{footer}</div>
      </DrawerRC>
    </React.Fragment>
  );
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
   * Позиция компонента
   */
  placement: PropTypes.string,
  onClose: PropTypes.func,
  onHandleClick: PropTypes.func,
  /**
   * Включение на маски(backdrop)
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
};

Drawer.defaultProps = {
  animation: true,
  backdropClosable: true,
  level: false,
};

export default Drawer;
