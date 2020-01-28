import React from 'react';
import PropTypes from 'prop-types';
import { compose, defaultProps, withHandlers } from 'recompose';
import isEmpty from 'lodash/isEmpty';
import Button from 'reactstrap/lib/Button';

export function MultiFieldsetItem({
  fields,
  render,
  rows,
  label,
  addButtonLabel,
  removeAllButtonLabel,
  needAddButton,
  needRemoveButton,
  needRemoveAllButton,
  needCopyButton,
  onAdd,
  onRemove,
  onRemoveAll,
  onCopy,
  canRemoveFirstItem,
  resolvePlaceholder,
}) {
  return (
    <>
      {fields.map((member, index) => (
        <div className="n2o-multi-fieldset__container">
          <div className="n2o-multi-fieldset__item">
            {label && (
              <div className="n2o-multi-fieldset__label">
                {resolvePlaceholder(index)}
              </div>
            )}
            {render(rows, member)}
            <div className="n2o-multi-fieldset__actions n2o-multi-fieldset__actions--inner">
              {needCopyButton && (
                <Button
                  className="n2o-multi-fieldset__copy"
                  color="link"
                  size="sm"
                  onClick={onCopy(index)}
                >
                  <i className="fa fa-copy" />
                </Button>
              )}
              {needRemoveButton && index > +!canRemoveFirstItem - 1 && (
                <Button
                  className="n2o-multi-fieldset__remove"
                  color="link"
                  size="sm"
                  onClick={onRemove(index)}
                >
                  <i className="fa fa-trash" />
                </Button>
              )}
            </div>
          </div>
        </div>
      ))}
      <div className="n2o-multi-fieldset__actions n2o-multi-fieldset__actions--common">
        {needAddButton && (
          <Button className="n2o-multi-fieldset__add" onClick={onAdd}>
            <i className="fa fa-plus mr-1" />
            {addButtonLabel}
          </Button>
        )}
        {!isEmpty(fields) && needRemoveAllButton && (
          <Button
            className="n2o-multi-fieldset__remove-all"
            onClick={onRemoveAll}
          >
            <i className="fa fa-trash mr-1" />
            {removeAllButtonLabel}
          </Button>
        )}
      </div>
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
  canRemoveFirstItem: PropTypes.bool,
};

const defaultComponentProps = {
  render: () => {},
  rows: [],
  label: null,
  addButtonLabel: 'Добавить',
  removeAllButtonLabel: 'Удалить все',
  needAddButton: true,
  needRemoveButton: true,
  needRemoveAllButton: false,
  needCopyButton: false,
  canRemoveFirstItem: false,
};

MultiFieldsetItem.defaultProps = defaultComponentProps;

const enhance = compose(
  defaultProps(defaultComponentProps),
  withHandlers({
    onAdd: ({ fields }) => () => fields.push({}),
    onRemove: ({ fields }) => index => () => fields.remove(index),
    onRemoveAll: ({ fields, canRemoveFirstItem }) => () =>
      fields.splice(+!canRemoveFirstItem, fields.length),
    onCopy: ({ fields }) => index => () => fields.push(fields.get(index)),
    resolvePlaceholder: ({ label }) => value =>
      label.replace('{value}', value + 1),
  })
);

export default enhance(MultiFieldsetItem);
