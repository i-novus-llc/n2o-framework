import React from 'react';
import Drawer from 'rc-drawer';
import PropTypes from 'prop-types';
import { compose, defaultProps } from 'recompose';
import cx from 'classnames';

/**
 * Drawer
 * @reactProps {string} className - className компонента
 * @reactProps {boolean} visible - состояние Drawer
 * @reactProps {boolean} closable - закрытие Drawer по клику на маску
 * @reactProps {string} placement - позиция Drawer
 * @reactProps {function} onClose - вызывается при закрытии
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

export function N2ODrawer(props) {
  const {
    className,
    closable,
    visible,
    placement,
    onClose,
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
        maskClosable={closable}
      >
        <div className="p-3">
          <div className="drawer-title">{title}</div>
          <div className="drawer-children">{children}</div>
          <div className="drawer-footer">{footer}</div>
        </div>
      </Drawer>
    </React.Fragment>
  );
}

N2ODrawer.propTypes = {
  className: PropTypes.string,
  closable: PropTypes.bool,
  visible: PropTypes.bool,
  placement: PropTypes.string,
  onClose: PropTypes.func,
  backdrop: PropTypes.bool,
  animation: PropTypes.bool,
  width: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  height: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  title: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
  footer: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
  children: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
};

const enchance = compose(
  defaultProps({
    closable: true,
    animation: true,
  })
);

export default enchance(N2ODrawer);
