import moment from 'moment/moment'
// eslint-disable-next-line no-restricted-imports
import _ from 'lodash'
import numeral from 'numeral'
import { isEmptyModel } from '@i-novus/n2o-components/lib/utils/isEmptyModel'

import globalFnDate from './globalFnDate'
import { guid as uuid } from './id'

export const functions = { moment, _, numeral, $: { ...globalFnDate.getFns(), uuid, isEmptyModel } }
export default { moment, _, numeral, $: { ...globalFnDate.getFns(), uuid, isEmptyModel } }
