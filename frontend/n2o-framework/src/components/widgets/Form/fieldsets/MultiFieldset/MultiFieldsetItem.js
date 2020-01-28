import React from 'react';
import PropTypes from 'prop-types';
import { compose, withHandlers } from 'recompose';
import isEmpty from 'lodash/isEmpty';
import Button from 'reactstrap/lib/Button';

export function MultiFieldsetItem({
  fields,
  render,
  rows,
  label,
  addButtonLabel,
  removeButtonLabel,
  removeAllButtonLabel,
  copyButtonLabel,
  needAddButton,
  needRemoveButton,
  needRemoveAllButton,
  needCopyButton,
  onAdd,
  onRemove,
  onRemoveAll,
}) {
  return (
    <>
      {fields.map((member, index) => (
        <div className="n2o-multi-fieldset__item">
          {label && <div className='n2o-multi-fieldset__label'>{`${label} ${++index}`}</div>}
          {render(rows, member)}
          <div className="n2o-multi-fieldset__actions">
            {needRemoveButton && index > 0 && (
              <Button className='n2o-multi-fieldset__remove' onClick={onRemove(index)}>{removeButtonLabel}</Button>
            )}
            {needCopyButton && <Button className='n2o-multi-fieldset__copy'>{copyButtonLabel}</Button>}
          </div>
        </div>
      ))}
      {!isEmpty(fields) && (
        <div className="n2o-multi-fieldset__actions n2o-multi-fieldset__actions--commons">
          {needAddButton && <Button className='n2o-multi-fieldset__add' onClick={onAdd}>{addButtonLabel}</Button>}
          {needRemoveAllButton && (
            <Button className='n2o-multi-fieldset__remove-all' onClick={onRemoveAll}>{removeAllButtonLabel}</Button>
          )}
        </div>
      )}
    </>
  );
}

MultiFieldsetItem.propTypes = {
  fields: PropTypes.object,
  render: PropTypes.func,
  rows: PropTypes.array,
  label: PropTypes.string,
  addButtonLabel: PropTypes.string,
  removeButtonLabel: PropTypes.string,
  removeAllButtonLabel: PropTypes.string,
  copyButtonLabel: PropTypes.string,
  needAddButton: PropTypes.bool,
  needRemoveButton: PropTypes.bool,
  needRemoveAllButton: PropTypes.bool,
  needCopyButton: PropTypes.bool,
};

MultiFieldsetItem.defaultProps = {
  render: () => {},
  rows: [],
  label: null,
  addButtonLabel: 'Добавить',
  removeButtonLabel: 'Удалить',
  removeAllButtonLabel: 'Удалить все',
  copyButtonLabel: 'Копировать',
  needAddButton: true,
  needRemoveButton: true,
  needRemoveAllButton: false,
  needCopyButton: false,
};

const enhance = compose(
  withHandlers({
    onAdd: ({ fields }) => () => fields.push({}),
    onRemove: ({ fields }) => index => () => fields.remove(index),
    onRemoveAll: ({ fields }) => () => fields.splice(1, fields.length),
  })
);

export default enhance(MultiFieldsetItem);
