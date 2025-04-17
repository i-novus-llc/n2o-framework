import React, { Component, ComponentType } from 'react'
import map from 'lodash/map'
import filter from 'lodash/filter'
import some from 'lodash/some'
import isArray from 'lodash/isArray'
import toString from 'lodash/toString'
import { connect } from 'react-redux'
import flowRight from 'lodash/flowRight'

import Tree from '../component/Tree'
import { ModelPrefix } from '../../../../core/datasource/const'
import { getModelByPrefixAndNameSelector } from '../../../../ducks/models/selectors'
import { type WithWidgetHandlersProps } from '../types'
import { State } from '../../../../ducks/State'
import { EMPTY_ARRAY } from '../../../../utils/emptyTypes'

export const defaultProps = {
    disabled: false,
    loading: false,
    datasource: EMPTY_ARRAY,
    parentIcon: '',
    childIcon: '',
    multiselect: false,
    showLine: false,
    filter: '',
    expandBtn: false,
    bulkData: false,
    parentFieldId: 'parentId',
    valueFieldId: 'id',
    labelFieldId: 'label',
    iconFieldId: 'icon',
    badgeFieldId: 'badge',
    badgeColorFieldId: 'color',
    hasCheckboxes: false,
    prefixCls: 'n2o-rc-tree',
    icon: '',
    childrenFieldId: 'children',
}

const withWidgetHandlers = <P extends object>(WrappedComponent: ComponentType<P & WithWidgetHandlersProps>) => {
    class Wrapper extends Component<P & WithWidgetHandlersProps> {
        onRowClickAction = () => {
            const { rowClick, dispatch } = this.props

            if (rowClick) {
                dispatch(rowClick.action)
            }
        }

        onResolve = (keys: string[]) => {
            const {
                setResolve,
                datasource,
                valueFieldId,
                multiselect,
                rowClick,
                dispatch,
            } = this.props

            const value = filter(datasource, data => some(keys, key => String(key) === String(data[valueFieldId])))

            if (multiselect) {
                setResolve(value)
            } else {
                setResolve(value ? value[0] : null)
            }

            if (rowClick) {
                dispatch(rowClick.action)
            }
        }

        render() {
            return (
                <WrappedComponent
                    {...this.props}
                    onRowClickAction={this.onRowClickAction}
                    onResolve={this.onResolve}
                />
            )
        }
    }

    return Wrapper
}

type ToStringData = { valueFieldId: string, parentFieldId: string }

const toStringData = ({ valueFieldId, parentFieldId }: ToStringData) => (dt: Record<string, string>) => ({
    ...dt,
    [valueFieldId]: dt[valueFieldId] && toString(dt[valueFieldId]),
    [parentFieldId]: dt[parentFieldId] && toString(dt[parentFieldId]),
})

const mapToString = (data: Record<string, string>, params: ToStringData) => (isArray(data) ? map(data, toStringData(params)) : toStringData(params)(data))

const mapStateToProps = (state: State, props: WithWidgetHandlersProps & { datasource: string } & ToStringData) => {
    const datasourceModel = getModelByPrefixAndNameSelector(ModelPrefix.source, props.datasource, [])(state) as unknown as Record<string, string>
    const resolveModel = getModelByPrefixAndNameSelector(ModelPrefix.active, props.datasource, {})(state) as Record<string, string>

    return {
        datasource: mapToString(datasourceModel, props),
        resolveModel: mapToString(resolveModel, props),
        selectedId: resolveModel.id,
    }
}

export const TreeContainer = flowRight(
    connect(mapStateToProps),
    withWidgetHandlers,
)(Tree)

TreeContainer.defaultProps = defaultProps

export default TreeContainer
