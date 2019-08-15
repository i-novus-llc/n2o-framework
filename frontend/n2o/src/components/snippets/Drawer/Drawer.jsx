import React from 'react';
import Drawer from 'rc-drawer';
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

function N2ODrawer(props) {
  const {
    className,
    closable,
    backdropClosable,
    visible,
    placement,
    onClose,
    onHandleClick,
    backdrop,
    animation,
    width,
    height,
    title,
    footer,
    children,
  } = props;
  return (
    <React.Fragment>
      <Drawer
        className={cx('n2o-drawer', animation && 'drawer-animation', className)}
        open={visible}
        width={width}
        height={height}
        placement={placement}
        onClose={onClose}
        showMask={backdrop}
        level={null}
        maskClosable={backdropClosable}
        onHandleClick={onHandleClick}
        handler={closable}
      >
        <div className="drawer-title">{title}</div>
        <div className="drawer-children">{children}</div>
        <div className="drawer-footer">{footer}</div>
      </Drawer>
    </React.Fragment>
  );
}

N2ODrawer.propTypes = {
  className: PropTypes.string,
  closable: PropTypes.bool,
  backdropClosable: PropTypes.bool,
  visible: PropTypes.bool,
  placement: PropTypes.string,
  onClose: PropTypes.func,
  onHandleClick: PropTypes.func,
  backdrop: PropTypes.bool,
  animation: PropTypes.bool,
  width: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  height: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  title: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
  footer: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
  children: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
};

N2ODrawer.defaultProps = {
  animation: true,
  backdropClosable: true,
};

export default N2ODrawer;
