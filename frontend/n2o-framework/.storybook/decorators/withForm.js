import React, { Fragment } from 'react';
import Factory from '../../src/core/factory/Factory';
import {WIDGETS} from "../../src/core/factory/factoryLevels";
import defaultFormJson from '../json/defaultForm';
import { set, get, merge } from 'lodash';

const renderForm = json => <Factory level={WIDGETS} {...json['Page_Form']} id="Page_Form" />;

const defaultPath = 'Page_Form.form.fieldsets[0].rows[0].cols[0].fields[0].control';

export default ( options ) => propsFn => () => {

  const formJson = get(options, 'formJson', defaultFormJson);
  const pathToReplace = get(options, 'pathToReplace', defaultPath);
  const src = get(options, 'src', 'InputSelect');

  const props = propsFn() || {};

  return renderForm(set(formJson, pathToReplace, { ...props, src }));
}







