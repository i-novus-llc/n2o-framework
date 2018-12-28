import React from 'react';
import PropTypes from 'prop-types';
import { Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { compose } from 'recompose';
import Page from './Page';
import { connect } from 'react-redux';
import cn from 'classnames';
import { createStructuredSelector } from 'reselect';
import { makePageLoadingByIdSelector, makePageTitleByIdSelector } from '../../selectors/pages';
import Actions from '../actions/Actions';
import factoryResolver from '../../utils/factoryResolver';
import withActions from './withActions';
import CoverSpinner from '../snippets/Spinner/CoverSpinner';

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
 * @example
 *  <ModalPage props={props}
 *             actions={actions}
 *             name={name}
 *             pageId={pageId}
 *  />
 */
class ModalPage extends React.Component {
  renderFromSrc(src) {
    const Component = factoryResolver(src, null);
    return <Component />;
  }

  render() {
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
      close,
      loading
    } = this.props;

    const pageMapping = {
      pathMapping,
      queryMapping
    };

    const showSpinner = typeof loading === 'undefined' || loading;
    const classes = cn({ 'd-none': showSpinner });
    return (
      <div className={'modal-page-overlay'}>
        {showSpinner && <CoverSpinner mode="transparent" />}
        <Modal
          isOpen={visible}
          toggle={close}
          size={size}
          backdrop={false}
          style={{
            zIndex: 10
          }}
        >
          <ModalHeader className={classes} toggle={close}>
            {title}
          </ModalHeader>
          <ModalBody className={classes}>
            {pageUrl ? (
              <Page pageUrl={pageUrl} pageId={pageId} pageMapping={pageMapping} />
            ) : src ? (
              this.renderFromSrc(src)
            ) : null}
          </ModalBody>
          {toolbar && (
            <ModalFooter className={classes}>
              <div className="n2o-modal-actions">
                <Actions
                  toolbar={toolbar.bottomLeft}
                  actions={actions}
                  containerKey={containerKey}
                />
                <Actions
                  toolbar={toolbar.bottomRight}
                  actions={actions}
                  containerKey={containerKey}
                />
              </div>
            </ModalFooter>
          )}
        </Modal>
      </div>
    );
  }
}

export const ModalWindow = ModalPage;

ModalPage.propTypes = {
  pageId: PropTypes.string,
  visible: PropTypes.bool,
  size: PropTypes.oneOf(['lg', 'sm']),
  title: PropTypes.string,
  closeButton: PropTypes.bool,
  toolbar: PropTypes.array,
  name: PropTypes.string,
  actions: PropTypes.object,
  props: PropTypes.object,
  close: PropTypes.func.isRequired
};

ModalPage.defaultProps = {
  size: 'lg'
};

const mapStateToProps = createStructuredSelector({
  title: (state, { pageId }) => makePageTitleByIdSelector(pageId)(state),
  loading: (state, { pageId }) => makePageLoadingByIdSelector(pageId)(state)
});

export default compose(
  connect(mapStateToProps),
  withActions
)(ModalPage);
