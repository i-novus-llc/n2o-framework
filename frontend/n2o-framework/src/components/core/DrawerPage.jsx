import React from 'react';
import PropTypes from 'prop-types';
import Drawer from '../snippets/Drawer/Drawer';
import { compose } from 'recompose';
import Page from './Page';
import cn from 'classnames';
import Spinner from '../snippets/Spinner/Spinner';
import Toolbar from '../buttons/Toolbar';
import withOverlayMethods from './withOverlayMethods';

/**
 * Компонент, отображающий Drawer
 * @reactProps {string} pageId - id пейджа
 * @reactProps {string} name - имя модалки
 * @reactProps {boolean} visible - отображается модалка или нет
 * @reactProps {string} title - заголовок в хэдере
 * @reactProps {object} actions - объект экшнов
 * @reactProps {array} toolbar - массив, описывающий внений вид кнопок-экшенов
 * @reactProps {object} props - аргументы для экшенов-функций
 * @reactProps {boolean}  disabled - блокировка модалки
 * @reactProps {function}  hidePrompt - скрытие окна подтверждения
 * @example
 *  <DrawerPage props={props}
 *             actions={actions}
 *             name={name}
 *             pageId={pageId}
 *  />
 */

function DrawerPage(props) {
  const {
    src,
    pageUrl,
    pageId,
    pathMapping,
    queryMapping,
    visible,
    loading,
    title,
    footer,
    disabled,
    toolbar,
    actions,
    containerKey,
    width,
    height,
    placement,
    backdrop,
    level,
    backdropClosable,
    animation,
    ...rest
  } = props;

  const pageMapping = {
    pathMapping,
    queryMapping,
  };

  const showSpinner = !visible || loading || typeof loading === 'undefined';
  const classes = cn({ 'd-none': loading });

  return (
    <div className="drawer-page-overlay">
      <Spinner
        className="drawer-spinner"
        loading={showSpinner}
        type="cover"
        color="light"
        transparent
      >
        <Drawer
          visible={!loading && visible}
          onHandleClick={() => rest.closeOverlay(true)}
          onClose={() => rest.closeOverlay(true)}
          title={title}
          backdrop={backdrop}
          width={width}
          height={height}
          placement={placement}
          level={level}
          backdropClosable={backdropClosable}
          animation={animation}
          footer={
            !!toolbar ? (
              <div
                className={cn('n2o-modal-actions', {
                  'n2o-disabled': disabled,
                })}
              >
                <Toolbar
                  toolbar={toolbar.bottomLeft}
                  containerKey={containerKey}
                />
                <Toolbar
                  toolbar={toolbar.bottomRight}
                  containerKey={containerKey}
                />
              </div>
            ) : (
              footer
            )
          }
        >
          <div className={classes}>
            {pageUrl ? (
              <Page
                pageUrl={pageUrl}
                pageId={pageId}
                pageMapping={pageMapping}
                containerKey={containerKey}
                needMetadata={true}
              />
            ) : src ? (
              rest.renderFromSrc(src)
            ) : null}
          </div>
        </Drawer>
      </Spinner>
    </div>
  );
}

export const DrawerWindow = DrawerPage;

DrawerPage.propTypes = {
  pageId: PropTypes.string,
  visible: PropTypes.bool,
  title: PropTypes.string,
  name: PropTypes.string,
  props: PropTypes.object,
  close: PropTypes.func.isRequired,
  disabled: PropTypes.bool,
};

DrawerPage.contextTypes = {
  defaultPromptMessage: PropTypes.string,
};

export default compose(withOverlayMethods)(DrawerPage);
