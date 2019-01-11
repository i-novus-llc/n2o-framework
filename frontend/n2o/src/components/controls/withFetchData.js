import React from 'react';
import PropTypes from 'prop-types';
import pathToRegexp from 'path-to-regexp';
import { fetchInputSelectData } from '../../core/api';
import cachingStore from '../../utils/cacher';
import { connect } from 'react-redux';
import { get, isArray, has, isEqual } from 'lodash';
import { addAlert, removeAlerts } from '../../actions/alerts';
import { getParams } from '../../utils/compileUrl';
import { createStructuredSelector } from 'reselect';
import { makeFormByName } from '../../selectors/formPlugin';

const DEPENDENCY_TYPES = {
  RE_RENDER: 'reRender'
};

/**
 * HOC для работы с данными
 * @param WrappedComponent - оборачиваемый компонент
 * @param apiCaller - promise для вызова апи
 */

function withFetchData(WrappedComponent, apiCaller = fetchInputSelectData) {
  class WithFetchData extends React.Component {
    constructor(props) {
      super(props);

      this.state = {
        data: [],
        isLoading: false,
        count: 0,
        size: props.size,
        page: 1
      };

      this._fetchData = this._fetchData.bind(this);
      this._mapping = this._mapping.bind(this);
      this._findResponseInCache = this._findResponseInCache.bind(this);
      this._fetchDataProvider = this._fetchDataProvider.bind(this);
      this._addAlertMessage = this._addAlertMessage.bind(this);
      this._setErrorMessage = this._setErrorMessage.bind(this);
      this._setResponseToData = this._setResponseToData.bind(this);
    }

    static getDerivedStateFromProps(nextProps) {
      if (nextProps.data && nextProps.data.length) {
        return {
          data: nextProps.data
        };
      }
    }

    componentDidUpdate(nextProps) {
      if (
        this.props.dependency &&
        this.isDependencyValueEqual(this.props, nextProps, this.props.dependency)
      ) {
        this._fetchData({
          size: this.props.size,
          [`sorting.${this.props.labelFieldId}`]: 'ASC'
        });
      }
    }

    isDependencyValueEqual(props, nextProps, dependency) {
      let reRenderDependency = false;
      let reRenderDependencyItem = null;
      for (let i = 0; i < dependency.length; i++) {
        if (dependency[i].type === DEPENDENCY_TYPES.RE_RENDER) {
          reRenderDependency = true;
          reRenderDependencyItem = dependency[i];
          break;
        }
      }
      if (reRenderDependency) {
        for (let i = 0; i < reRenderDependencyItem.on.length; i++) {
          if (
            !isEqual(
              props.dependencyValue[reRenderDependencyItem.on[i]],
              nextProps.dependencyValue[reRenderDependencyItem.on[i]]
            )
          ) {
            return true;
          }
        }
      }
      return false;
    }

    /**
     * Взятие данных для запроса по link или по контексту.
     * @param mappingConfig
     * @returns {{}}
     * @private
     */
    _mapping(mappingConfig) {
      return getParams(mappingConfig, this.context.store.getState());
    }

    /**
     * Поиск в кеше запроса
     * @param params
     * @returns {*}
     * @private
     */
    _findResponseInCache(params) {
      const { caching } = this.props;
      if (caching && cachingStore.find(params)) {
        return cachingStore.find(params);
      }
      return false;
    }

    /**
     * Вывод сообщения
     * @param message
     * @private
     */
    _addAlertMessage(messages) {
      const { addAlert, removeAlerts } = this.props;
      removeAlerts();
      isArray(messages)
        ? messages.map(m => addAlert({ ...m, closeButton: false }))
        : addAlert({ ...messages, closeButton: false });
    }

    /**
     * Вывод сообщения с ошибкой
     * @param response
     * @private
     */
    async _setErrorMessage({ response }) {
      const errorMessage = await response.json();
      const messages = get(errorMessage, 'meta.alert.messages', false);
      messages && this._addAlertMessage(messages);
    }

    /**
     * Взять данные с сервера с помощью dataProvider
     * @param pathMapping
     * @param queryMapping
     * @param url
     * @param extraParams
     * @returns {Promise<void>}
     * @private
     */
    async _fetchDataProvider({ pathMapping, queryMapping, url }, extraParams = {}) {
      const pathParams = this._mapping(pathMapping);
      const queryParams = this._mapping(queryMapping);
      const basePath = pathToRegexp.compile(url)(pathParams);
      return await apiCaller({ ...queryParams, ...extraParams }, null, { basePath });
    }

    /**
     * Обновить данные если запрос успешен
     * @param list
     * @param count
     * @param size
     * @param page
     * @private
     */
    _setResponseToData({ list, count, size, page }, concat = false) {
      this.setState({
        data: concat ? this.state.data.concat(list) : list,
        isLoading: false,
        count,
        size,
        page
      });
    }

    /**
     * Получает данные с сервера
     * @param extraParams - параметры запроса
     * @param concat - флаг объединения данных
     * @returns {Promise<void>}
     * @private
     */

    async _fetchData(extraParams = {}, concat = false) {
      const { dataProvider } = this.props;
      if (!dataProvider) return;

      this.setState({ loading: true });
      let response = this._findResponseInCache({ dataProvider, extraParams });
      if (!response) {
        try {
          response = await this._fetchDataProvider(dataProvider, extraParams);
          cachingStore.add({ dataProvider, extraParams }, response);
          if (has(response, 'message')) this._addAlertMessage(response.message);

          this._setResponseToData(response, concat);
        } catch (err) {
          await this._setErrorMessage(err);
        } finally {
          this.setState({ loading: false });
        }
      } else {
        this._setResponseToData(response, concat);
        this.setState({ loading: false });
      }
    }

    /**
     * Рендер
     */

    render() {
      return <WrappedComponent {...this.props} {...this.state} _fetchData={this._fetchData} />;
    }
  }

  WithFetchData.propTypes = {
    caching: PropTypes.bool,
    size: PropTypes.number
  };

  WithFetchData.contextTypes = { store: PropTypes.object };

  WithFetchData.defaultProps = {
    caching: false,
    size: 10
  };

  const mapStateToProps = createStructuredSelector({
    dependencyValue: (state, props) => {
      return makeFormByName(props.form)(state, props).values;
    }
  });

  const mapDispatchToProps = (dispatch, ownProps) => ({
    addAlert: message => dispatch(addAlert(ownProps.form + '.' + ownProps.labelFieldId, message)),
    removeAlerts: () => dispatch(removeAlerts(ownProps.form + '.' + ownProps.labelFieldId))
  });

  return connect(
    mapStateToProps,
    mapDispatchToProps
  )(WithFetchData);
}

export default withFetchData;
