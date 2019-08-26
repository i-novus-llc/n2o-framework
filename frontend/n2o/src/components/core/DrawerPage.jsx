import React from 'react';
import PropTypes from 'prop-types';
import Drawer from '../snippets/Drawer/Drawer';
import { createStructuredSelector } from 'reselect';
import {
  makePageDisabledByIdSelector,
  makePageLoadingByIdSelector,
  makePageTitleByIdSelector,
} from '../../selectors/pages';
import { makeShowPromptByName } from '../../selectors/overlays';
import { compose } from 'recompose';
import Page from './Page';
import { connect } from 'react-redux';
import withActions from './withActions';
import factoryResolver from '../../utils/factoryResolver';
import cn from 'classnames';
import Spinner from '../snippets/Spinner/Spinner';
import Actions from '../actions/Actions';

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

class DrawerPage extends React.Component {
  constructor(props) {
    super(props);
    this.closeDrawer = this.closeDrawer.bind(this);
    this.closePrompt = this.closePrompt.bind(this);
    this.showPrompt = this.showPrompt.bind(this);
  }

  renderFromSrc(src) {
    const Component = factoryResolver(src, null);
    return <Component />;
  }

  closeDrawer(prompt) {
    const { name, close } = this.props;
    close(name, prompt);
  }

  closePrompt() {
    const { name, hidePrompt } = this.props;
    hidePrompt(name);
  }

  showPrompt() {
    if (window.confirm(this.context.defaultPromptMessage)) {
      this.closeDrawer(false);
    } else {
      this.closePrompt();
    }
  }
  render() {
    const {
      src,
      showPrompt,
      pageUrl,
      pageId,
      pathMapping,
      queryMapping,
      visible,
      loading,
      title,
      disabled,
      toolbar,
      actions,
      containerKey,
    } = this.props;
    const pageMapping = {
      pathMapping,
      queryMapping,
    };
    const showSpinner = !visible || loading || typeof loading === 'undefined';
    const classes = cn({ 'd-none': loading });
    return (
      <div className="drawer-page-overlay">
        {showPrompt && this.showPrompt()}
        <Spinner
          className="drawer-spinner"
          loading={showSpinner}
          type="cover"
          color="light"
          transparent
        >
          <Drawer
            visible={!loading && visible}
            onHandleClick={() => this.closeDrawer(false)}
            title={title}
            backdrop={false}
            footer={
              toolbar && (
                <div
                  className={cn('n2o-modal-actions', {
                    'n2o-disabled': disabled,
                    classes,
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
              )
            }
          >
            <div className={classes}>
              {pageUrl ? (
                <Page
                  pageUrl={pageUrl}
                  pageId={pageId}
                  pageMapping={pageMapping}
                  needMetadata
                />
              ) : src ? (
                this.renderFromSrc(src)
              ) : null}
            </div>
          </Drawer>
        </Spinner>
      </div>
    );
  }
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

const mapStateToProps = createStructuredSelector({
  title: (state, { pageId }) => makePageTitleByIdSelector(pageId)(state),
  loading: (state, { pageId }) => makePageLoadingByIdSelector(pageId)(state),
  disabled: (state, { pageId }) => makePageDisabledByIdSelector(pageId)(state),
  showPrompt: (state, { name }) => makeShowPromptByName(name)(state),
});

export default compose(
  connect(mapStateToProps),
  withActions
)(DrawerPage);
