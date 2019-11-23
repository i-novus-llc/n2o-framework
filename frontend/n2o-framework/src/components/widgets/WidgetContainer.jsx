import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { compose, pure } from 'recompose';
import get from 'lodash/get';
import isEqual from 'lodash/isEqual';
import isEmpty from 'lodash/isEmpty';
import isFunction from 'lodash/isFunction';
import cx from 'classnames';
import { batchActions } from 'redux-batched-actions';
import { callActionImpl } from '../../actions/toolbar';
import Placeholder from '../snippets/Placeholder/Placeholder';

import {
  dataRequestWidget,
  registerWidget,
  removeWidget,
  resetWidgetState,
  resolveWidget,
  setActive,
  setTableSelectedId,
  sortByWidget,
} from '../../actions/widgets';
import { setModel, removeModel, removeAllModel } from '../../actions/models';
import { PREFIXES } from '../../constants/models';
import {
  makeGetModelByPrefixSelector,
  makeGetResolveModelSelector,
} from '../../selectors/models';
import {
  isAnyTableFocusedSelector,
  makeIsActiveSelector,
  makeSelectedIdSelector,
  makeTypeSelector,
  makeWidgetDataProviderSelector,
  makeWidgetEnabledSelector,
  makeWidgetIsInitSelector,
  makeWidgetLoadingSelector,
  makeWidgetSortingSelector,
  makeWidgetVisibleSelector,
} from '../../selectors/widgets';
import observeStore from '../../utils/observeStore';
import propsResolver from '../../utils/propsResolver';
import { removeAlerts } from '../../actions/alerts';
import Spinner from '../snippets/Spinner/Spinner';

const s = {};

/**
 * HOC, оборачивает в виджет контейнер, Выполняет запрос за данными и связь с redux
 * @param initialConfig
 * @param widgetType
 */
const createWidgetContainer = (initialConfig, widgetType) => {
  const config = {
    ...initialConfig,
  };

  /**
   * мэппинг пропосов
   * @param props
   */
  function mapProps(props) {
    if (isFunction(config.mapProps)) {
      return config.mapProps(props);
    } else {
      return {
        datasource: props.datasource,
        onResolve: props.onResolve,
      };
    }
  }

  function filterSelector(bindLink) {
    return store => get(store, bindLink);
  }

  /**
   * @reactProps {string} widgetId - идентификатор виджета
   * @reactProps {string} pageId - идентификатор страницы
   * @reactProps {boolean} fetchOnInit
   * @reactProps {number} size
   * @reactProps {object} filterDefaultValues
   * @reactProps {object} defaultSorting
   * @reactProps {object} dataProvider
   * @reactProps {object} validation
   * @reactProps {function} onSetFilter
   * @reactProps {boolean} visible
   * @reactProps {boolean} isLoading
   * @reactProps {object|array} datasource
   * @reactProps {object} resolveModel
   * @reactProps {object} sorting
   * @reactProps {function} onResolve
   * @reactProps {function} onFetch
   * @reactProps {function} dispatch
   * @reactProps {function} isInit
   * @reactProps {function} isActive
   */
  return WrappedComponent => {
    class WidgetContainer extends React.Component {
      constructor(props) {
        super(props);

        this.initIfNeeded();
        this.onFocus = this.onFocus.bind(this);
        this.onFetch = this.onFetch.bind(this);
        this.onResolve = this.onResolve.bind(this);
        this.onSort = this.onSort.bind(this);
        this.onSetModel = this.onSetModel.bind(this);
        this.onActionImpl = this.onActionImpl.bind(this);
      }

      componentDidMount() {
        const {
          fetchOnInit,
          visible,
          dataProviderFromState,
          dataProvider,
        } = this.props;
        if (
          fetchOnInit &&
          visible &&
          (isEqual(dataProvider, dataProviderFromState) ||
            !dataProviderFromState)
        ) {
          this.onFetch();
        }
      }

      componentDidUpdate(prevProps) {
        const { visible, dataProviderFromState } = this.props;

        if (
          (!prevProps.visible && visible) ||
          (!isEqual(prevProps.dataProviderFromState, dataProviderFromState) &&
            !isEmpty(prevProps.dataProviderFromState) &&
            !isEmpty(dataProviderFromState))
        ) {
          this.onFetch();
        }
      }

      /**
       * Диспатч экшена удаления виджета
       */
      componentWillUnmount() {
        const { widgetId, dispatch } = this.props;
        let actions = [
          removeAlerts(widgetId),
          removeAllModel(widgetId),
          setTableSelectedId(widgetId, null),
        ];
        dispatch(batchActions(actions));
      }

      onSetModel(prefix, widgetId, model) {
        const { dispatch } = this.props;
        dispatch(setModel(prefix, widgetId, model));
      }

      onResolve(newModel, oldModel) {
        const { widgetId, dispatch } = this.props;
        if (!isEqual(newModel, oldModel)) {
          dispatch(resolveWidget(widgetId, newModel));
        }
      }

      onSort(id, direction) {
        const { widgetId, isActive, dispatch } = this.props;
        dispatch(sortByWidget(widgetId, id, direction));
        dispatch(dataRequestWidget(widgetId));
        !isActive && dispatch(setActive(widgetId));
      }

      onFocus() {
        const { widgetId, dispatch } = this.props;
        dispatch(setActive(widgetId));
      }

      onFetch(options) {
        const { widgetId, dispatch } = this.props;
        dispatch(dataRequestWidget(widgetId, options));
      }

      onActionImpl({ src, component, options }) {
        const { dispatch } = this.props;
        dispatch(callActionImpl(src || component, { ...options, dispatch }));
      }

      /**
       * Диспатч экшена регистрации виджета
       */
      initIfNeeded() {
        const {
          dispatch,
          isInit,
          widgetId,
          pageId,
          size,
          page,
          defaultSorting,
          validation,
          dataProvider,
          dataProviderFromState,
        } = this.props;
        if (!isInit || !isEqual(dataProvider, dataProviderFromState)) {
          dispatch(
            registerWidget(widgetId, {
              pageId,
              size,
              type: widgetType,
              page,
              sorting: defaultSorting,
              dataProvider,
              validation,
            })
          );
        }
      }

      /**
       *Базовый рендер
       */
      render() {
        const { visible, isLoading, placeholder } = this.props;
        const propsToPass = mapProps({
          ...this.props,
          onSetModel: this.onSetModel,
          onResolve: this.onResolve,
          onFocus: this.onFocus,
          onFetch: this.onFetch,
          onSort: this.onSort,
          onActionImpl: this.onActionImpl,
        });
        const style = {
          position: 'relative',
        };

        return (
          <div
            className={cx(
              visible ? s.visible : s.hidden,
              isLoading ? s.loading : ''
            )}
            style={style}
          >
            <Placeholder
              once={true}
              loading={placeholder && isLoading}
              {...placeholder}
            >
              <Spinner loading={isLoading} type="cover">
                <WrappedComponent {...propsToPass} />
              </Spinner>
            </Placeholder>
          </div>
        );
      }
    }

    WidgetContainer.propTypes = {
      /* manual */
      widgetId: PropTypes.string,
      pageId: PropTypes.string,
      fetchOnInit: PropTypes.bool,
      placeholder: PropTypes.oneOfType(PropTypes.bool, PropTypes.object),
      size: PropTypes.number,
      page: PropTypes.number,
      filterDefaultValues: PropTypes.object,
      defaultSorting: PropTypes.object,
      dataProvider: PropTypes.object,
      validation: PropTypes.object,
      onSetFilter: PropTypes.func,
      /* redux */
      visible: PropTypes.bool,
      isLoading: PropTypes.bool,
      datasource: PropTypes.oneOfType([PropTypes.object, PropTypes.array]),
      resolveModel: PropTypes.object,
      sorting: PropTypes.object,
      onResolve: PropTypes.func,
      onFetch: PropTypes.func,
      dispatch: PropTypes.func,
      isInit: PropTypes.bool,
      isActive: PropTypes.bool,
    };

    WidgetContainer.defaultProps = {
      fetchOnInit: true,
      visible: true,
      isLoading: false,
      resolveModel: {},
      defaultSorting: {},
      placeholder: false,
    };

    WidgetContainer.contextTypes = {
      store: PropTypes.object,
    };

    const mapStateToProps = (state, props) => {
      return {
        isInit: makeWidgetIsInitSelector(props.widgetId)(state, props),
        visible: makeWidgetVisibleSelector(props.widgetId)(state, props),
        isEnabled: makeWidgetEnabledSelector(props.widgetId)(state),
        isLoading: makeWidgetLoadingSelector(props.widgetId)(state, props),
        isAnyTableFocused: isAnyTableFocusedSelector(state, props),
        datasource: makeGetModelByPrefixSelector('datasource', props.widgetId)(
          state,
          props
        ),
        resolveModel: makeGetResolveModelSelector(props.widgetId)(state, props),
        activeModel: makeGetModelByPrefixSelector(
          props.modelPrefix,
          props.widgetId
        )(state, props),
        sorting: makeWidgetSortingSelector(props.widgetId)(state, props),
        selectedId: makeSelectedIdSelector(props.widgetId)(state, props),
        defaultSorting: props.sorting,
        isActive: makeIsActiveSelector(props.widgetId)(state, props),
        type: makeTypeSelector(props.widgetId)(state, props),
        dataProviderFromState: makeWidgetDataProviderSelector(props.widgetId)(
          state
        ),
      };
    };

    function mapDispatchToProps(dispatch) {
      return {
        dispatch,
      };
    }

    return compose(
      connect(
        mapStateToProps,
        mapDispatchToProps
      ),
      pure
    )(WidgetContainer);
  };
};

export default createWidgetContainer;
