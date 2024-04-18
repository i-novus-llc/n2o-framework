import React from 'react'
import classNames from 'classnames'
import { compose } from 'recompose'
import { HotKeys } from 'react-hotkeys/cjs'
import PropTypes from 'prop-types'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import cloneDeep from 'lodash/cloneDeep'
import get from 'lodash/get'
import set from 'lodash/set'

import { Text } from '../../../../snippets/Text/Text'
import withCell from '../../withCell'
import withTooltip from '../../withTooltip'
import { DefaultCell } from '../DefaultCell'

import withActionsEditableCell from './withActionsEditableCell'
/**
 * Компонент редактируемой ячейки таблицы
 * @reactProps {boolean} visible - флаг видимости
 * @reactProps {object} control - настройки компонента фильтрации
 * @reactProps {boolean} editable - флаг разрешения редактирования
 * @reactProps {string} model - значение модели
 * @reactProps {boolean} disabled - флаг активности
 * @reactProps {string} strategy - стиль вывода popper fixed
 */
export class EditableCell extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            model: props.model || {},
            editing: false,
            prevModel: {},
        }

        this.onChange = this.onChange.bind(this)
        this.toggleEdit = this.toggleEdit.bind(this)
        this.handleKeyDown = this.handleKeyDown.bind(this)
        this.callAction = this.callAction.bind(this)
    }

    componentDidUpdate(prevProps, prevState) {
        const { model: propsModel, editable, prevResolveModel, editFieldId } = this.props
        const { model: stateModel, editing, prevModel } = this.state

        // Нужно для того, чтобы сбросить внутренний стейт компонента, когда очищается propsModel
        const isPropsModelCleared = (
            isEmpty(propsModel[editFieldId]) &&
            !isEmpty(prevModel[editFieldId]) &&
            !isEqual(propsModel[editFieldId], stateModel[editFieldId])
        )

        if (
            prevProps.editable !== editable &&
            !isEqual(prevProps.model, propsModel) &&
            !isEqual(stateModel, prevResolveModel)
        ) {
            this.setState({ model: propsModel })
        } else if (!isEqual(prevProps.model, propsModel)) {
            this.setState({
                prevModel: stateModel,
                model: isEmpty(prevResolveModel)
                    ? propsModel
                    : prevResolveModel,
            })
        } else if (
            !isEqual(prevProps.prevResolveModel, prevResolveModel) &&
            prevResolveModel.id === stateModel.id
        ) {
            this.setState({
                prevModel: stateModel,
                model: prevResolveModel,
            })
        } else if (isPropsModelCleared) {
            this.setState({
                model: propsModel,
            })
        }

        if (
            !editing &&
            isEqual(prevState.prevModel, prevState.model) &&
            !isEqual(prevModel, stateModel) &&
            !isEqual(stateModel, prevResolveModel)
        ) {
            this.callAction(stateModel)
        }
    }

    onChange(value) {
        const { model: stateModel } = this.state
        const newModel = cloneDeep(stateModel)
        const { editFieldId } = this.props

        set(newModel, editFieldId, value)
        this.setState({
            model: newModel,
        })
    }

    toggleEdit() {
        const { editing, prevModel, model: stateModel } = this.state
        let newState = {
            editing: !editing,
        }

        if (!newState.editing && !isEqual(prevModel, stateModel)) {
            this.callAction(stateModel)
        }

        newState = {
            ...newState,
            prevModel: stateModel,
        }

        this.setState(newState)
    }

    callAction(model) {
        const { callAction, onResolve, modelId } = this.props

        onResolve(modelId, model)
        callAction(model)
    }

    handleKeyDown() {
        this.toggleEdit()
    }

    stopPropagation = (e) => {
        e.stopPropagation()
    }

    render() {
        const {
            visible,
            control,
            editable,
            disabled,
            format,
            fieldKey,
            editFieldId,
            model: propsModel,
        } = this.props
        const { editing, model: stateModel } = this.state
        const events = { events: 'enter' }
        const handlers = { events: this.handleKeyDown }
        const text = get(propsModel, fieldKey)

        return (
            visible && (
                <DefaultCell
                    disabled={disabled}
                    className={classNames({ 'n2o-editable-cell': editable })}
                    onClick={this.stopPropagation}
                >
                    {!editing && (
                        <div
                            className={classNames('n2o-editable-cell-text', {
                                'editable-cell-empty': !text && text !== 0,
                            })}
                            onClick={editable && this.toggleEdit}
                        >
                            <Text text={text} format={format} />
                        </div>
                    )}
                    {editable && editing && (
                        <HotKeys keyMap={events} handlers={handlers}>
                            <div className="n2o-editable-cell-control">
                                {React.createElement(control.component, {
                                    ...control,
                                    className: 'n2o-advanced-table-edit-control',
                                    onChange: this.onChange,
                                    onBlur: this.toggleEdit,
                                    autoFocus: true,
                                    value: get(stateModel, editFieldId),
                                    openOnFocus: true,
                                    showButtons: false,
                                    resetOnNotValid: false,
                                    strategy: 'fixed',
                                })}
                            </div>
                        </HotKeys>
                    )}
                </DefaultCell>
            )
        )
    }
}

EditableCell.propTypes = {
    visible: PropTypes.bool,
    control: PropTypes.object,
    editable: PropTypes.bool,
    disabled: PropTypes.bool,
    editFieldId: PropTypes.string,
    model: PropTypes.object,
    prevResolveModel: PropTypes.object,
    callAction: PropTypes.func,
    onResolve: PropTypes.func,
    // eslint-disable-next-line react/no-unused-prop-types
    widgetId: PropTypes.string,
    modelId: PropTypes.string,
    format: PropTypes.string,
    fieldKey: PropTypes.string,
}

EditableCell.defaultProps = {
    visible: true,
    disabled: false,
    model: {},
    editFieldId: null,
}

export default compose(
    withActionsEditableCell,
    withCell,
    withTooltip,
)(EditableCell)
