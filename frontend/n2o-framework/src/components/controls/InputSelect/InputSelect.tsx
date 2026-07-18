import React, { useMemo, useCallback, ComponentType, UIEvent } from 'react'
import { PopupList } from '@i-novus/n2o-components/lib/display/PopupList/PopupList'
import classNames from 'classnames'
import isEmpty from 'lodash/isEmpty'
import throttle from 'lodash/throttle'
import find from 'lodash/find'
import { sortByAvailability } from '@i-novus/n2o-components/lib/inputs/utils'

import { listContainer } from '../listContainer'
import { PopUp } from '../../snippets/PopUp/PopUp'
import { NOOP_FUNCTION } from '../../../utils/emptyTypes'

import { type InputSelectProps, type Option } from './types'
import { getSelectedOptions, normalizeMultiSelected, computeFormattedOptions, getDynamicListHeight, isScrollEnd } from './helpers/common'
import { MultiSelect } from './MultiSelect'
import { SingleSelect } from './SingleSelect'
import { DefaultIcons } from './const'
import { SelectPostfix } from './SelectPostfix'
import { useSingleSelect } from './hooks/useSingleSelect'
import { useMultiSelect } from './hooks/useMultiSelect'

const Component = ({
    loading = false,
    disabled = false,
    disabledValues = [],
    value: defaultValue,
    resetOnBlur = false,
    filter = false,
    multiSelect = false,
    closePopupOnSelect = true,
    hasCheckboxes = false,
    valueFieldId = 'id',
    openOnFocus,
    options: propsOptions = [],
    descriptionFieldId = '',
    enabledFieldId = '',
    statusFieldId = '',
    labelFieldId = 'name',
    inputLabelFieldId = labelFieldId,
    iconFieldId = '',
    imageFieldId = '',
    groupFieldId = '',
    fetchData,
    onSearch,
    onFocus,
    onBlur: propsOnBlur,
    onChange,
    format,
    badge,
    placeholder,
    quickSearchParam,
    id,
    model,
    className,
    popUpFullSize,
    readOnly,
    enableCustomTags,
    maxTagCount,
    size,
    page,
    count,
    cleanable = true,
    toggleOnInputClick = true,
    selectFormatFew,
    selectFormatMany,
    selectFormatOne,
    selectFormat,
    maxTagTextLength,
    getSearchMinLengthHint,
    sortFieldId,
    onKeyDown: propsOnKeyDown,
}: InputSelectProps) => {
    const selected = model?.[id]
    const filterType = filter === 'server' ? false : filter

    const options = useMemo(
        () => computeFormattedOptions(propsOptions, format),
        [propsOptions, format],
    )

    const commonProps = {
        options: propsOptions,
        inputLabelFieldId,
        fetchData,
        onSearch,
        closePopupOnSelect,
        openOnFocus,
        onBlur: propsOnBlur,
        resetOnBlur,
        popUpFullSize,
        toggleOnInputClick,
        loading,
        quickSearchParam,
        enabledFieldId,
        disabled,
        propsOnKeyDown,
        defaultValue,
    }

    const singleSelectProps = useSingleSelect({
        ...commonProps,
        onChange: value => onChange(value),
        selected: multiSelect ? null : (selected as Option | null),
        labelFieldId,
    })

    const multiSelectProps = useMultiSelect({
        ...commonProps,
        onChange: value => onChange(value),
        selected: multiSelect ? sortByAvailability(normalizeMultiSelected(selected), enabledFieldId) : [],
        enableCustomTags,
        maxTagCount,
        summaryFormat: {
            selectFormatFew,
            selectFormatMany,
            selectFormatOne,
            selectFormat,
        },
    })

    const {
        open,
        inputValue,
        inputRef,
        inputComponentRef,
        popUpRef,
        popUpItemRef,
        tagsRef,
        selectPostfixRef,
        onInputClick,
        onToggle,
        triggerWidth,
        triggerRef,
        activeValueId,
        onTriggerClick,
        onInput,
        onSelect,
        onClear,
        onKeyDown,
        onBlur,
    } = multiSelect ? multiSelectProps : singleSelectProps

    const onRemoveItem = multiSelect ? multiSelectProps.onRemoveItem : undefined
    const onTagRemove = multiSelect ? multiSelectProps.onTagRemove : undefined

    const readOnlyControls = !cleanable || isEmpty(selected)

    const postfix = useMemo(
        () => (
            <SelectPostfix
                className={classNames({ open, 'read-only': readOnlyControls })}
                onClear={readOnlyControls ? NOOP_FUNCTION : onClear}
                onToggle={onToggle}
                clear={DefaultIcons.CLEAR}
                toggle={DefaultIcons.TOGGLE_DOWN}
                loading={loading}
                ref={selectPostfixRef}
                visible={!disabled}
            />
        ),
        [disabled, loading, onClear, onToggle, open, readOnlyControls, selectPostfixRef],
    )

    const listHeightStyle = getDynamicListHeight({ count, size, page })
    const popUplistStyle = popUpFullSize ? { width: triggerWidth, ...listHeightStyle } : listHeightStyle

    // TODO пока копипаста из старого компонента, для PopupList, нужно перепроверить для чего это
    const sorting = !isEmpty(sortFieldId) && !isEmpty(inputValue)
    const searchMinLengthHint = getSearchMinLengthHint?.()

    const filterValue = useMemo(
        () => ({ [quickSearchParam || labelFieldId]: inputValue }),
        [quickSearchParam, labelFieldId, inputValue],
    )

    const needAddFilter = useMemo(
        () => (!isEmpty(filter) || sorting) && !find(selected, (item: Option) => item[labelFieldId] === inputValue),
        [filter, sorting, selected, labelFieldId, inputValue],
    )

    const throttledFetch = useMemo(
        () => throttle(
            (page, filter) => fetchData({ page, ...filter }, true),
            200,
            { leading: true, trailing: false },
        ),
        [fetchData],
    )

    const onScroll = useCallback((event: UIEvent<HTMLDivElement>) => {
        const target = event.currentTarget

        if (isScrollEnd(target) && !loading && page * size < count) {
            const filter = needAddFilter ? filterValue : {}

            throttledFetch(page + 1, filter)
        }
    }, [count, loading, needAddFilter, page, size, filterValue, throttledFetch])

    return (
        <PopUp
            open={open}
            className={classNames('n2o-input-select__menu', { open, 'pop-up-full-size': popUpFullSize })}
            triggerClassName={classNames('n2o-input-select', className, {
                multi: multiSelect,
                single: !multiSelect,
                'read-only': readOnly,
                disabled,
            })}
            triggerRef={triggerRef}
            onTriggerClick={onTriggerClick}
        >
            <PopUp.Trigger>
                {multiSelect ? (
                    <MultiSelect
                        value={inputValue}
                        tags={multiSelectProps.tags}
                        maxTagTextLength={maxTagTextLength}
                        onInput={onInput}
                        onFocus={onFocus}
                        onInputClick={onInputClick}
                        onBlur={onBlur}
                        placeholder={placeholder}
                        disabled={disabled}
                        inputRef={inputRef}
                        inputComponentRef={inputComponentRef}
                        className="form-control"
                        tagsRef={tagsRef}
                        onKeyDown={onKeyDown}
                        postfix={postfix}
                        onTagRemove={onTagRemove}
                        readOnly={readOnly}
                        id={id}
                    />
                ) : (
                    <SingleSelect
                        value={inputValue}
                        onInput={onInput}
                        onFocus={onFocus}
                        onInputClick={onInputClick}
                        onBlur={onBlur}
                        placeholder={placeholder}
                        disabled={disabled}
                        inputRef={inputRef}
                        inputComponentRef={inputComponentRef}
                        onKeyDown={onKeyDown}
                        className="form-control"
                        postfix={postfix}
                        readOnly={readOnly}
                        id={id}
                    />
                )}
            </PopUp.Trigger>
            <PopUp.Content>
                <PopupList
                    ref={popUpRef}
                    popUpItemRef={popUpItemRef}
                    labelFieldId={labelFieldId}
                    valueFieldId={valueFieldId}
                    options={options}
                    iconFieldId={iconFieldId}
                    imageFieldId={imageFieldId}
                    descriptionFieldId={descriptionFieldId}
                    groupFieldId={groupFieldId}
                    enabledFieldId={enabledFieldId}
                    statusFieldId={statusFieldId}
                    hasCheckboxes={hasCheckboxes}
                    onSelect={onSelect}
                    onRemoveItem={onRemoveItem}
                    badge={badge}
                    loading={loading}
                    disabledValues={disabledValues}
                    selected={getSelectedOptions(selected, multiSelect)}
                    style={popUplistStyle}
                    activeValueId={activeValueId}
                    multiSelect={multiSelect}
                    searchMinLengthHint={searchMinLengthHint}
                    onScroll={onScroll}
                />
            </PopUp.Content>
        </PopUp>
    )
}

Component.displayName = 'InputSelectComponent'

export const InputSelect = listContainer<InputSelectProps>(Component) as ComponentType<InputSelectProps>
InputSelect.displayName = 'InputSelect'

export default InputSelect
