import React from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { get } from 'lodash';
import { connect } from 'react-redux';
import { compose, withContext, lifecycle } from 'recompose';
import numeral from 'numeral';
import 'numeral/locales/ru';
import {
  requestConfig as requestConfigAction,
  setReady as setReadyAction,
} from '../../actions/global';
import { globalSelector } from '../../selectors/global';
import Spinner from '../snippets/Spinner/Spinner';

numeral.locale('ru');

function Application({ ready, locale, loading, messages, render }) {
  return (
    <Spinner type="cover" loading={loading}>
      {ready && render(locale, messages)}
    </Spinner>
  );
}

Application.propTypes = {
  ready: PropTypes.bool,
  locale: PropTypes.string,
  messages: PropTypes.object,
  menu: PropTypes.object,
  loading: PropTypes.bool,
  realTimeConfig: PropTypes.bool,
  render: PropTypes.func,
  requestConfig: PropTypes.func,
};

const mapStateToProps = state => ({
  ...globalSelector(state),
});

const mapDispatchToProps = dispatch => ({
  setReady: bindActionCreators(setReadyAction, dispatch),
  requestConfig: bindActionCreators(requestConfigAction, dispatch),
});

export default compose(
  connect(
    mapStateToProps,
    mapDispatchToProps
  ),
  withContext(
    {
      getLocale: PropTypes.func,
      getMessages: PropTypes.func,
      getMenu: PropTypes.object,
      getFromConfig: PropTypes.func,
    },
    props => ({
      getLocale: () => props.locale,
      getMessages: () => props.messages,
      getMenu: props.menu,
      getFromConfig: key => get(props, key),
    })
  ),
  lifecycle({
    componentDidMount() {
      const { realTimeConfig, requestConfig, setReady } = this.props;
      if (realTimeConfig) {
        requestConfig();
      } else {
        setReady();
      }
    },
  })
)(Application);
