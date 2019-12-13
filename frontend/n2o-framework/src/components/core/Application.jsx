import React from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import get from 'lodash/get';
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

function Application({ ready, loading, render, ...config }) {
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
});

export default compose(
  connect(
    mapStateToProps,
    mapDispatchToProps
  ),
  withContext(
    {
      getFromConfig: PropTypes.func,
    },
    props => ({
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
