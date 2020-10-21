import React from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import get from 'lodash/get';
import map from 'lodash/map';
import keys from 'lodash/keys';
import { connect } from 'react-redux';
import {
  compose,
  withContext,
  lifecycle,
  withHandlers,
} from 'recompose';
import numeral from 'numeral';
import 'numeral/locales/ru';
import {
  requestConfig as requestConfigAction,
  setReady as setReadyAction,
  registerLocales,
} from '../../actions/global';
import { globalSelector } from '../../selectors/global';
import Spinner from '../snippets/Spinner/Spinner';

function Application(props) {
  const { ready, loading, render, ...config } = props;
  numeral.locale(props.locale);

  return (
    <Spinner type="cover" loading={loading}>
      {ready && render(config)}
    </Spinner>
  );
}

Application.propTypes = {
  ready: PropTypes.bool,
  loading: PropTypes.bool,
  realTimeConfig: PropTypes.bool,
  render: PropTypes.func,
};

const mapStateToProps = state => ({
  ...globalSelector(state),
});

const mapDispatchToProps = dispatch => ({
  setReady: bindActionCreators(setReadyAction, dispatch),
  requestConfig: bindActionCreators(requestConfigAction, dispatch),
  registerLocales: locales => dispatch(registerLocales(locales)),
});

export default compose(
  connect(
    mapStateToProps,
    mapDispatchToProps
  ),
  withContext(
    {
      getFromConfig: PropTypes.func,
      configLocale: PropTypes.string,
    },
    props => ({
      getFromConfig: key => get(props, key),
      configLocale: props.locale,
    })
  ),
  withHandlers({
    addCustomLocales: ({ i18n, customLocales }) => () => {
      map(keys(customLocales), locale => {
        i18n.addResourceBundle(locale, 'translation', customLocales[locale]);
      });
    },
  }),
  lifecycle({
    componentDidMount() {
      const {
        realTimeConfig,
        requestConfig,
        setReady,
        locales = {},
        customLocales,
        registerLocales,
        addCustomLocales,
      } = this.props;
      addCustomLocales();
      registerLocales(keys(Object.assign({}, locales, customLocales)));

      if (realTimeConfig) {
        requestConfig();
      } else {
        setReady();
      }
    },
    componentDidUpdate(prevProps) {
      const { locale, i18n } = this.props;
      if (prevProps.locale !== locale) {
        i18n.changeLanguage(locale);
      }
    },
  })
)(Application);
