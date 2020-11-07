import React from 'react';
import PropTypes from 'prop-types';
import isEmpty from 'lodash/isEmpty';
import map from 'lodash/map';
import Button from 'reactstrap/lib/Button';

export function MultiFieldsetItem({
  fields,
  render,
  rows,
  label,
  parentName,
  addButtonLabel,
  removeAllButtonLabel,
  needAddButton,
  needRemoveButton,
  needRemoveAllButton,
  needCopyButton,
  canRemoveFirstItem,
  resolvePlaceholder,
  onAddField,
  onRemoveField,
  onRemoveAll,
  onCopyField,
  enabled,
}) {
  return (
    <React.Fragment>
      {map(fields, (field, index) => (
        <div className="n2o-multi-fieldset__container">
          <div className="n2o-multi-fieldset__item">
            {label && (
              <div className="n2o-multi-fieldset__label">
                {resolvePlaceholder(index)}
              </div>
            )}
            {render(rows, {
              parentName: `${parentName}[${index}]`,
              parentIndex: index,
            })}
            <div className="n2o-multi-fieldset__actions n2o-multi-fieldset__actions--inner">
              {needCopyButton && enabled && (
                <Button
                  className="n2o-multi-fieldset__copy"
                  color="link"
                  size="sm"
                  onClick={onCopyField(index)}
                >
                  <i className="fa fa-copy" />
                </Button>
              )}
              {needRemoveButton &&
              index > +!canRemoveFirstItem - 1 &&
              enabled && (
                <Button
                  className="n2o-multi-fieldset__remove"
                  color="link"
                  size="sm"
                  onClick={onRemoveField(index)}
                >
                  <i className="fa fa-trash" />
                </Button>
              )}
            </div>
          </div>
        </div>
      ))}
      <div className="n2o-multi-fieldset__actions n2o-multi-fieldset__actions--common">
        {needAddButton && enabled && (
          <Button className="n2o-multi-fieldset__add" onClick={onAddField}>
            <i className="fa fa-plus mr-1" />
            {addButtonLabel}
          </Button>
        )}
        {!isEmpty(fields) && needRemoveAllButton && enabled && (
          <Button
            className="n2o-multi-fieldset__remove-all"
            onClick={onRemoveAll}
          >
            <i className="fa fa-trash mr-1" />
            {removeAllButtonLabel}
          </Button>
        )}
      </div>
    </React.Fragment>
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
  fields: [],
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

export default MultiFieldsetItem;
