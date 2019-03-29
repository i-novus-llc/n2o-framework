import React from 'react';
import PropTypes from 'prop-types';
import _ from 'lodash';
import { FormattedMessage } from 'react-intl';

import SelectContainer from '../../controls/Select/SelectContainer';
import RadioGroupContainer from '../../controls/RadioGroup/RadioGroupContainer';
import ReduxForm from '../Form/ReduxForm';
import Fieldset from '../Form/Fieldset';

import { Row, Col } from 'reactstrap';

/**
 * Имя формы экспорта таблицы
 * @type {string}
 */
export const exportFormName = 'exportTableForm';

/**
 * Тело модального экна экспорта таблицы
 * @reactProps {Object} type - объект формата скачиваемого файла
 * @reactProps {Object} code - объект кодировок
 */
class ExportModal extends React.Component {
  /**
   * инициализация типов файла и кодировок
   * @param props
   */
  constructor(props) {
    super(props);
    this.type = {
      csv: 'CSV',
      dbf: 'DBF',
      xlsx: 'XLSX',
      xml: 'XML',
      txt: 'TXT',
      ...props.type,
    };
    this.code = {
      cp1251: 'cp1251',
      'utf-8': 'UTF-8',
      ...props.code,
    };
    this.size = {
      all: (
        <FormattedMessage
          id="exportTable.downloadAll"
          defaultMessage="Загрузить всё"
        />
      ),
      current: (
        <FormattedMessage
          id="exportTable.downloadCurrent"
          defaultMessage="Текущая страница"
        />
      ),
    };
    this.defaults = {
      type: 'csv',
      code: 'cp1251',
      size: 'all',
    };
  }

  /**
   * Обработка новых проспов
   * @param newProps
   */
  componentWillReceiveProps(newProps) {
    this.type = {
      ...this.type,
      ...newProps.type,
    };
    this.code = {
      ...this.code,
      ...newProps.code,
    };
  }

  /**
   * Базовый рендер
   */
  render() {
    const { type, code, size } = this;
    const selectControl = { component: SelectContainer };
    const radioControl = { component: RadioGroupContainer };
    return (
      <ReduxForm form={exportFormName} datasource={this.defaults}>
        <Fieldset>
          <Row>
            <Col md={12}>
              <ReduxForm.Field
                control={selectControl}
                name="type"
                label="Формат"
                options={type}
              />
            </Col>
          </Row>
          <Row>
            <Col md={12}>
              <ReduxForm.Field
                control={selectControl}
                name="code"
                label="Кодировка"
                options={code}
              />
            </Col>
          </Row>
          <Row>
            <Col md={12}>
              <ReduxForm.Field
                control={radioControl}
                name="size"
                radios={size}
              />
            </Col>
          </Row>
        </Fieldset>
      </ReduxForm>
    );
  }
}

ExportModal.propTypes = {
  type: PropTypes.object,
  code: PropTypes.code,
};

export default ExportModal;
