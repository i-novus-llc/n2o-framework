import React from 'react';
import PropTypes from 'prop-types';
import Modal from 'reactstrap/lib/Modal';
import ModalHeader from 'reactstrap/lib/ModalHeader';
import ModalBody from 'reactstrap/lib/ModalBody';
import ModalFooter from 'reactstrap/lib/ModalFooter';
import { compose } from 'recompose';
import Page from './Page';
import cn from 'classnames';
import Actions from '../actions/Actions';
import Spinner from '../snippets/Spinner/Spinner';
import withOverlayMethods from './withOverlayMethods';

/**
 * Компонент, отображающий модальное окно
 * @reactProps {string} pageId - id пейджа
 * @reactProps {string} name - имя модалки
 * @reactProps {boolean} visible - отображается модалка или нет
 * @reactProps {string} size - размер('sm' или 'lg')
 * @reactProps {string} title - заголовок в хэдере
 * @reactProps {boolean} closeButton - Есть кнопка закрытия или нет
 * @reactProps {object} actions - объект экшнов
 * @reactProps {array} toolbar - массив, описывающий внений вид кнопок-экшенов
 * @reactProps {object} props - аргументы для экшенов-функций
 * @reactProps {boolean}  disabled - блокировка модалки
 * @reactProps {function}  hidePrompt - скрытие окна подтверждения
 * @example
 *  <ModalPage props={props}
 *             actions={actions}
 *             name={name}
 *             pageId={pageId}
 *  />
 */
function ModalPage(props) {
  const {
    pageUrl,
    pageId,
    src,
    pathMapping,
    queryMapping,
    size,
    actions,
    containerKey,
    toolbar,
    visible,
    title,
    loading,
    disabled,
    ...rest
  } = props;

  const pageMapping = {
    pathMapping,
    queryMapping,
  };

  const showSpinner = !visible || loading || typeof loading === 'undefined';
  const classes = cn({ 'd-none': loading });
  return (
    <div className={cn('modal-page-overlay')}>
      <Spinner type="cover" loading={showSpinner} color="light" transparent>
        <Modal
          isOpen={visible}
          toggle={() => rest.closeOverlay(false)}
          size={size}
          backdrop={false}
          style={{
            zIndex: 10,
          }}
        >
          <ModalHeader
            className={classes}
            toggle={() => rest.closeOverlay(false)}
          >
            {title}
          </ModalHeader>
          <ModalBody className={classes}>
            {pageUrl ? (
              <Page
                pageUrl={pageUrl}
                pageId={pageId}
                pageMapping={pageMapping}
                needMetadata
              />
            ) : src ? (
              rest.renderFromSrc(src)
            ) : null}
          </ModalBody>
          {toolbar && (
            <ModalFooter className={classes}>
              <div
                className={cn('n2o-modal-actions', {
                  'n2o-disabled': disabled,
                })}
              >
                <Actions
                  toolbar={toolbar.bottomLeft}
                  actions={actions}
                  containerKey={containerKey}
                  pageId={pageId}
                />
                <Actions
                  toolbar={toolbar.bottomRight}
                  actions={actions}
                  containerKey={containerKey}
                  pageId={pageId}
                />
              </div>
            </ModalFooter>
          )}
        </Modal>
      </Spinner>
    </div>
  );
}

export const ModalWindow = ModalPage;

ModalPage.propTypes = {
  /**
   * ID страницы
   */
  pageId: PropTypes.string,
  /**
   * Видимость модального окна
   */
  visible: PropTypes.bool,
  /**
   * Размер окна
   */
  size: PropTypes.oneOf(['lg', 'sm']),
  /**
   * Заголовок
   */
  title: PropTypes.string,
  /**
   * Включение кнопки закрытия
   */
  closeButton: PropTypes.bool,
  /**
   * Настройка кнопок
   */
  toolbar: PropTypes.array,
  /**
   * Название модалки
   */
  name: PropTypes.string,
  /**
   * Объект экшенов
   */
  actions: PropTypes.object,
  props: PropTypes.object,
  /**
   * Функция закрытия
   */
  close: PropTypes.func.isRequired,
  /**
   * Флаг активности
   */
  disabled: PropTypes.bool,
};

ModalPage.defaultProps = {
  size: 'lg',
  disabled: false,
};

ModalPage.contextTypes = {
  defaultPromptMessage: PropTypes.string,
  resolveProps: PropTypes.func,
};

export default compose(withOverlayMethods)(ModalPage);
