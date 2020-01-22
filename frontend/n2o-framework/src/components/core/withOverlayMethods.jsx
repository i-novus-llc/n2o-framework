import React from 'react';
import { createStructuredSelector } from 'reselect';
import {
  makePageDisabledByIdSelector,
  makePageLoadingByIdSelector,
  makePageTitleByIdSelector,
} from '../../selectors/pages';
import { makeShowPromptByName } from '../../selectors/overlays';
import { compose } from 'recompose';
import { connect } from 'react-redux';
import withActions from './withActions';

function withOverlayMethods(WrappedComponent) {
  class OverlayMethods extends React.Component {
    constructor(props) {
      super(props);
      this.closeOverlay = this.closeOverlay.bind(this);
      this.closePrompt = this.closePrompt.bind(this);
      this.showPrompt = this.showPrompt.bind(this);
    }

    componentDidUpdate(prevProps) {
      if (
        this.props.showPrompt !== prevProps.showPrompt &&
        this.props.showPrompt
      ) {
        this.showPrompt();
      }
    }
    renderFromSrc(src) {
      const { resolveProps } = this.context;
      const Component = resolveProps(src, null);
      return <Component />;
    }

    closeOverlay(prompt) {
      const { name, close } = this.props;
      close(name, prompt);
    }

    closePrompt() {
      const { name, hidePrompt } = this.props;
      hidePrompt(name);
    }

    showPrompt() {
      if (window.confirm(this.context.defaultPromptMessage)) {
        this.closeOverlay(false);
      } else {
        this.closePrompt();
      }
    }

    render() {
      return (
        // {this.props.showPrompt && this.showPrompt()}
        <WrappedComponent
          {...this.props}
          closeOverlay={this.closeOverlay}
          renderFromSrc={this.renderFromSrc}
        />
      );
    }
  }

  const mapStateToProps = createStructuredSelector({
    title: (state, { pageId }) => makePageTitleByIdSelector(pageId)(state),
    loading: (state, { pageId }) => makePageLoadingByIdSelector(pageId)(state),
    disabled: (state, { pageId }) =>
      makePageDisabledByIdSelector(pageId)(state),
    showPrompt: (state, { name }) => makeShowPromptByName(name)(state),
  });

  return compose(
    connect(mapStateToProps),
    withActions
  )(OverlayMethods);
}

export default withOverlayMethods;
