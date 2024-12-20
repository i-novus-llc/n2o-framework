import React from 'react'
import { bindActionCreators, Dispatch } from 'redux'
import { connect } from 'react-redux'
import isEqual from 'lodash/isEqual'
import each from 'lodash/each'
import concat from 'lodash/concat'
import isEmpty from 'lodash/isEmpty'
import isNil from 'lodash/isNil'
import classNames from 'classnames'

import { setMultiFieldVisible, setMultiFieldDisabled } from '../../../ducks/form/store'
import { WithPropsResolver } from '../../../core/Expression/withResolver'
import { FormContext } from '../../core/FormProvider/provider'

import FieldsetRow from './FieldsetRow'
import { type FieldsetComponentProps, RowProps } from './types'

/**
 * Компонент - филдсет формы
 * @reactProps {array} rows - ряды, которые содержит филдсет. Они содержат колонки, которые содержат либо поля, либо филдсеты(филдсет рекрсивный).
 * @reactProps {string} className - класс компонента Fieldset
 * @reactProps {string} help - подсказка около label
 * @reactProps {string} labelPosition - позиция лейбела относительно контрола: top-left, top-right, left, right.
 * @reactProps {string} label - заголовок филдсета
 * @reactProps {string} childrenLabel - заголовоки дочерних элементов  филдсета
 * @reactProps {array} labelWidth - ширина лейбела - Либо число, либо 'min' - займет минимальное возможное пространство, либо default - 100px
 * @reactProps {array} labelAlignment - выравнивание текста внутри лейбла
 * @reactProps {number} defaultCol
 * @reactProps {number|string} autoFocusId
 * @reactProps {node} component
 * @reactProps {node} children
 * @example
 *
 * //пример структуры rows
 * const rows = [
 *    {
 *      "cols": [
 *        {
 *          "fields": [
 *            {
 *            //...
 *            }
 *          ]
 *        },
 *        {
 *          "fields": [
 *            {
 *            //...
 *            }
 *          ]
 *        },
 *        {
 *          "fields": [
 *            {
 *            //...
 *            }
 *          ]
 *        },
 *      ]
 *    },
 *    {
 *      "cols": [
 *        {
 *          "fieldsets": [
 *            {
 *            "rows": [
 *            //...
 *            ]
 *            }
 *          ]
 *        },
 *        {
 *          "fields": [
 *            {
 *            //...
 *            },
 *            {
 *            //...
 *            }
 *          ]
 *        },
 *      ]
 *    }
 *  ]
 *
 *  <Fieldset rows={rows}>
 *
 */

export interface State {
    visible: boolean
    enabled: boolean
}

class Fieldset extends React.Component<FieldsetComponentProps, State> {
    fields: string[]

    fieldsVisibility: boolean[]

    constructor(props: FieldsetComponentProps) {
        super(props)
        this.state = { visible: true, enabled: true }

        this.fields = []
        // параметры visible всех полей филдcета
        // необходимо для того, чтобы отличить hidden филдсет (n2o-fieldset empty)
        this.fieldsVisibility = []
    }

    componentDidUpdate() {
        const { visible, enabled, activeModel, propsResolver } = this.props
        const {
            enabled: enabledFromState,
            visible: visibleFromState,
        } = this.state

        const newEnabled = isNil(enabled) || propsResolver?.(enabled, activeModel) as boolean
        const newVisible = isNil(visible) || propsResolver?.(visible, activeModel) as boolean

        if (!isEqual(newEnabled, enabledFromState)) {
            this.setEnabled(newEnabled)
        }

        if (!isEqual(newVisible, visibleFromState)) {
            this.setVisible(newVisible)
        }
    }

    setVisible = (nextVisibleField: boolean) => {
        const { setMultiFieldVisible } = this.props
        const { formName } = this.context

        this.setState(() => {
            if (setMultiFieldVisible) { setMultiFieldVisible(formName, this.fields, nextVisibleField) }

            return {
                visible: nextVisibleField,
            }
        })
    }

    setEnabled = (nextEnabledField: boolean) => {
        const { setMultiFieldDisabled } = this.props
        const { formName } = this.context

        if (setMultiFieldDisabled) { setMultiFieldDisabled(formName, this.fields, !nextEnabledField) }

        this.setState({
            enabled: nextEnabledField,
        })
    }

    calculateAllFields(rows: RowProps[]) {
        /** @type {{fieldsVisibility: *[], fields: *[]}} */
        const info = { fields: [], fieldsVisibility: [] } as { fields: string[], fieldsVisibility: boolean[] }

        each(rows, (row) => {
            each(row.cols, (col) => {
                if (col.fieldsets) {
                    each(col.fieldsets, (fieldset) => {
                        const subInfo = this.calculateAllFields(fieldset.rows)

                        info.fields = concat(info.fields, subInfo.fields)
                        info.fieldsVisibility = concat(info.fieldsVisibility, subInfo.fieldsVisibility)
                    })
                } else if (col.fields) {
                    each(col.fields, (field) => {
                        info.fields.push(field.id)
                        info.fieldsVisibility.push(Boolean(field.visible))
                    })
                }
            })
        })

        return info
    }

    checkGlobalFieldVisibility = (fieldsVisibility: boolean[]) => fieldsVisibility.some(visible => visible)

    renderRow = (rowId: number, row: RowProps, props: Record<string, unknown>) => {
        const {
            labelPosition = 'top-left',
            labelWidth,
            labelAlignment,
            defaultCol,
            autoFocusId,
            modelPrefix,
            autoSubmit,
            activeModel,
            onChange,
            onBlur,
        } = this.props
        const { formName } = this.context
        const { enabled } = this.state

        return (
            <FieldsetRow
                activeModel={activeModel}
                key={rowId}
                row={row}
                rowId={rowId}
                labelPosition={labelPosition}
                labelWidth={labelWidth}
                labelAlignment={labelAlignment}
                defaultCol={defaultCol}
                autoFocusId={autoFocusId}
                form={formName}
                modelPrefix={modelPrefix}
                disabled={!enabled}
                autoSubmit={autoSubmit}
                onChange={onChange}
                onBlur={onBlur}
                {...props}
            />
        )
    }

    render() {
        const {
            className,
            style,
            component: ElementType = 'div',
            children,
            parentName,
            label,
            description,
            type,
            childrenLabel,
            activeModel,
            help,
            propsResolver,
            ...rest
        } = this.props

        const { enabled, visible } = this.state

        this.fields = []

        const needLabel = !!label && type !== 'line'
        const needDescription = !!description && type !== 'line'

        if (React.Children.count(children)) {
            return <ElementType>{children}</ElementType>
        }

        const classes = classNames('n2o-fieldset', className, {
            'd-none': !visible,
            empty: !isEmpty(this.fieldsVisibility) && !this.checkGlobalFieldVisibility(this.fieldsVisibility),
        })

        const resolvedLabel = propsResolver?.(label, activeModel) as string
        const resolvedHelp = propsResolver?.(help, activeModel) as string

        return (
            <ElementType
                className={classes}
                style={style}
                needLabel={needLabel}
                needDescription={needDescription}
                childrenLabel={childrenLabel}
                enabled={enabled}
                label={resolvedLabel}
                type={type}
                activeModel={activeModel}
                description={description}
                {...rest}
                render={(rows, props = { parentName }) => {
                    const { fields, fieldsVisibility } = this.calculateAllFields(rows)

                    this.fields = fields
                    this.fieldsVisibility = fieldsVisibility

                    return rows?.map((row, index) => this.renderRow(index, row, props))
                }}
                help={resolvedHelp}
            />
        )
    }
}

Fieldset.contextType = FormContext

const mapDispatchToProps = (dispatch: Dispatch) => bindActionCreators(
    { setMultiFieldVisible, setMultiFieldDisabled },
    dispatch,
)

const Connected = connect(null, mapDispatchToProps)(Fieldset)

export const FieldsetContainer = WithPropsResolver<FieldsetComponentProps>(Connected)
