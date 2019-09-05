import React from 'react';
import PropTypes from 'prop-types';
import {
  compose,
  withState,
  lifecycle,
  setDisplayName,
  withHandlers,
  withProps,
  defaultProps,
} from 'recompose';

import ReactAutocomplete from 'react-autocomplete';
import AutoCompleteItem from './AutoCompleteItem';

import { get, isFunction } from 'lodash';
import cn from 'classnames';

function AutoComplete({
  value,
  onChange,
  options,
  getItemValue,
  shouldItemRender,
  renderItem,
  className,
  style,
  inputProps,
  onSelect,
  ...rest
}) {
  return (
    <div className={cn('n2o-autocomplete', className)}>
      <ReactAutocomplete
        value={value}
        onChange={onChange}
        items={options}
        getItemValue={getItemValue}
        shouldItemRender={shouldItemRender}
        wrapperStyle={style}
        renderItem={renderItem}
        inputProps={inputProps}
        onSelect={onSelect}
      />
    </div>
  );
}

AutoComplete.propTypes = {
  value: PropTypes.string,
  onChange: PropTypes.func,
  options: PropTypes.array,
  style: PropTypes.object,
  labelFieldId: PropTypes.string,
};

AutoComplete.defaultProps = {
  options: [],
  style: {},
  labelFieldId: 'label',
};

const enhance = compose(
  setDisplayName('AutoComplete'),
  defaultProps({
    labelFieldId: 'label',
  }),
  withProps(() => ({
    inputProps: {
      className: 'form-control',
    },
  })),
  withState('value', 'setValue', ''),
  withHandlers({
    getItemValue: ({ labelFieldId }) => item => get(item, labelFieldId, ''),
    renderItem: ({ labelFieldId }) => (item, isHighlighted) => (
      <AutoCompleteItem
        value={get(item, labelFieldId)}
        isHighlighted={isHighlighted}
      />
    ),
    shouldItemRender: ({ labelFieldId }) => (item, value) =>
      !!~get(item, labelFieldId, '')
        .toLowerCase()
        .indexOf(value.toLowerCase()),
    onChange: ({ onChange, setValue }) => event => {
      const value = get(event, 'target.value', '');

      setValue(value);

      if (isFunction(onChange)) {
        onChange(value);
      }
    },
    onSelect: ({ onChange, setValue }) => value => {
      setValue(value);

      if (isFunction(onChange)) {
        onChange(value);
      }
    },
  })
);

export { AutoComplete };
export default enhance(AutoComplete);
