import moment from 'moment/moment'
import _ from 'lodash'
import numeral from 'numeral'
import { isEmptyModel } from '@i-novus/n2o-components/lib/utils/isEmptyModel'

import globalFnDate from './globalFnDate'
import { guid as uuid } from './id'

export default { moment, _, numeral, $: { ...globalFnDate.getFns(), uuid, isEmptyModel } }
