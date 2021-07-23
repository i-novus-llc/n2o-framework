import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { compose, pure, withProps } from 'recompose'
import keys from 'lodash/keys'
import filter from 'lodash/filter'
import map from 'lodash/map'
import includes from 'lodash/includes'
import pick from 'lodash/pick'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import isFunction from 'lodash/isFunction'
import has from 'lodash/has'
import cx from 'classnames'
import { batchActions } from 'redux-batched-actions'

import { callActionImpl } from '../../actions/toolbar'
import Placeholder from '../snippets/Placeholder/Placeholder'
import {
    dataRequestWidget,
    registerWidget,
    resolveWidget,
    setActive,
    setTableSelectedId,
    sortByWidget,
} from '../../actions/widgets'
import { setModel, removeAllModel } from '../../actions/models'
import {
    makeGetModelByPrefixSelector,
    makeGetResolveModelSelector,
} from '../../selectors/models'
import {
    isAnyTableFocusedSelector,
    makeWidgetByIdSelector,
} from '../../selectors/widgets'
import { removeAlerts } from '../../actions/alerts'
import Spinner from '../snippets/Spinner/Spinner'
import { InitMetadataContext } from '../../core/dependency'

const s = {}

/**
 * HOC, оборачивает в виджет контейнер, Выполняет запрос за данными и связь с redux
 * @param initialConfig
 * @param widgetType
 */
const createWidgetContainer = (initialConfig, widgetType) => {
    const config = {
        ...initialConfig,
    }

    /**
   * мэппинг пропосов
   * @param props
   */
    function mapProps(props) {
        if (isFunction(config.mapProps)) {
            return config.mapProps(props)
        }

        return {
            datasource: props.datasource,
            onResolve: props.onResolve,
        }
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
    return (WrappedComponent) => {
        class WidgetContainer extends React.Component {
            constructor(props, context) {
                super(props, context)

                this.initIfNeeded(context.metadata)
                this.onFocus = this.onFocus.bind(this)
                this.onFetch = this.onFetch.bind(this)
                this.onResolve = this.onResolve.bind(this)
                this.onSort = this.onSort.bind(this)
                this.onSetModel = this.onSetModel.bind(this)
                this.onActionImpl = this.onActionImpl.bind(this)
            }

            componentDidMount() {
                const {
                    fetchOnInit,
                    visible,
                    dataProviderFromState,
                    dataProvider,
                } = this.props
                const hasVisibleDeps = has(this.context, 'metadata.dependency.visible')

                if (
                    (hasVisibleDeps || fetchOnInit) &&
                    visible &&
                    (isEqual(dataProvider, dataProviderFromState) ||
                        !dataProviderFromState ||
                        isEmpty(dataProviderFromState))
                ) {
                    this.onFetch()
                }
            }

            componentDidUpdate(prevProps) {
                const { visible, dataProviderFromState } = this.props

                if (
                    (!prevProps.visible && visible) ||
                    (!isEqual(prevProps.dataProviderFromState, dataProviderFromState) &&
                        !isEmpty(prevProps.dataProviderFromState) &&
                        !isEmpty(dataProviderFromState))
                ) {
                    this.onFetch()
                }
            }

            /**
             * Диспатч экшена удаления виджета
             */
            componentWillUnmount() {
                const { widgetId, dispatch } = this.props
                const actions = [
                    removeAlerts(widgetId),
                    removeAllModel(widgetId),
                    setTableSelectedId(widgetId, null),
                ]

                dispatch(batchActions(actions))
            }

            isEqualRegisteredWidgetWithProps = () => {
                const { widget } = this.props
                const propsParamsNames = keys(this.props)
                const widgetParamsNames = filter(keys(widget), key => key !== 'error')
                const commonParamsNames = []

                map(widgetParamsNames, (widgetParamName) => {
                    if (includes(propsParamsNames, widgetParamName)) {
                        commonParamsNames.push(widgetParamName)
                    }
                })

                const widgetStateParams = pick(widget, commonParamsNames)
                const widgetPropsParams = pick(this.props, commonParamsNames)

                return isEqual(widgetStateParams, widgetPropsParams)
            };

            onSetModel(prefix, widgetId, model) {
                const { dispatch } = this.props

                dispatch(setModel(prefix, widgetId, model))
            }

            onResolve(newModel, oldModel) {
                const { widgetId, dispatch } = this.props

                if (!isEqual(newModel, oldModel)) {
                    dispatch(resolveWidget(widgetId, newModel))
                }
            }

            onSort(id, direction) {
                const { widgetId, isActive, dispatch } = this.props

                dispatch(sortByWidget(widgetId, id, direction))
                dispatch(dataRequestWidget(widgetId))
                !isActive && dispatch(setActive(widgetId))
            }

            onFocus() {
                const { widgetId, dispatch } = this.props

                dispatch(setActive(widgetId))
            }

            onFetch(options) {
                const { widgetId, dispatch } = this.props

                dispatch(dataRequestWidget(widgetId, options))
            }

            /**
             * @deprecated
             */
            onActionImpl({ src, component, options }) {
                const { dispatch } = this.props

                dispatch(callActionImpl(src || component, { ...options, dispatch }))
            }

            /**
             * Диспатч экшена регистрации виджета
             */
            initIfNeeded(initMetadata) {
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
                    modelPrefix,
                } = this.props

                const { visible: defaultVisible } = initMetadata

                if (!isInit || !this.isEqualRegisteredWidgetWithProps()) {
                    dispatch(
                        registerWidget(widgetId, {
                            pageId,
                            size,
                            type: widgetType,
                            page,
                            sorting: defaultSorting,
                            dataProvider,
                            validation,
                            modelPrefix,
                            isVisible: defaultVisible,
                        }),
                    )
                }
            }

            /**
             * Базовый рендер
             */
            render() {
                const { visible, isLoading, placeholder } = this.props
                const propsToPass = mapProps({
                    ...this.props,
                    onSetModel: this.onSetModel,
                    onResolve: this.onResolve,
                    onFocus: this.onFocus,
                    onFetch: this.onFetch,
                    onSort: this.onSort,
                    onActionImpl: this.onActionImpl,
                })
                const style = {
                    position: 'relative',
                }

                return (
                    <div
                        className={cx(
                            visible ? s.visible : s.hidden,
                            isLoading ? s.loading : '',
                        )}
                        style={style}
                    >
                        <Placeholder
                            once
                            loading={placeholder && isLoading}
                            {...placeholder}
                        >
                            <Spinner loading={isLoading} type="cover">
                                <WrappedComponent {...propsToPass} />
                            </Spinner>
                        </Placeholder>
                    </div>
                )
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
        }

        WidgetContainer.defaultProps = {
            fetchOnInit: true,
            visible: true,
            isLoading: false,
            resolveModel: {},
            defaultSorting: {},
            placeholder: false,
        }

        WidgetContainer.contextType = InitMetadataContext

        const mapStateToProps = (state, props) => ({
            widget: makeWidgetByIdSelector(props.widgetId)(state, props),
            isAnyTableFocused: isAnyTableFocusedSelector(state, props),
            datasource: makeGetModelByPrefixSelector('datasource', props.widgetId)(
                state,
                props,
            ),
            resolveModel: makeGetResolveModelSelector(props.widgetId)(state, props),
            activeModel: makeGetModelByPrefixSelector(
                props.modelPrefix,
                props.widgetId,
            )(state, props),
            defaultSorting: props.sorting,
        })

        function mapDispatchToProps(dispatch) {
            return {
                dispatch,
            }
        }

        return compose(
            connect(
                mapStateToProps,
                mapDispatchToProps,
            ),
            withProps(({ widget }) => ({
                isInit: widget.isInit,
                visible: widget.isVisible,
                isEnabled: widget.isEnabled,
                isLoading: widget.isLoading,
                sorting: widget.sorting,
                selectedId: widget.selectedId,
                isActive: widget.isActive,
                type: widget.type,
                dataProviderFromState: widget.dataProvider,
            })),
            pure,
        )(WidgetContainer)
    }
}

export default createWidgetContainer
