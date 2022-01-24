import React, { Component } from "react";
import { render } from "react-dom";
import { Button, ButtonGroup, ButtonToolbar } from 'reactstrap'
import N2O from "n2o";
import { id } from "n2o-framework/lib/utils/id";

import AppTemplate from "./AppTemplate";

import "n2o-framework/dist/n2o.css";
import "../../css/n2o-notifications.css";

import { reducer, sagas } from "../../src";
import { add, clearAll, setCounter } from "../../src/actions";
import pkg from "../../package.json";

const config = {
  defaultTemplate: AppTemplate,
  customReducers: {
    notifications: reducer
  },
  customSagas: [sagas]
};

class Demo extends Component {
  constructor(props) {
    super(props);
    this.count = 0;
    this.n2oRef = React.createRef();
    this.handleAddNotify = this.handleAddNotify.bind(this);
    this.handleClearAllNotify = this.handleClearAllNotify.bind(this);
    this.handleIncreaseCounter = this.handleIncreaseCounter.bind(this);
    this.handleResetCounter = this.handleResetCounter.bind(this);
  }

  componentDidMount() {
    this.n2oRef.current.store.dispatch(
      setCounter("testCounterId", this.count)
    );
  }

  handleAddNotify() {
    this.n2oRef.current.store.dispatch(
      add(id(), {
        title: "Hi",
        body: "World"
      })
    );
  }

  handleClearAllNotify() {
    this.n2oRef.current.store.dispatch(clearAll());
  }

  handleIncreaseCounter(rate = 1) {
    return () => {
      this.count += rate;
      this.n2oRef.current.store.dispatch(
        setCounter("testCounterId", this.count)
      );
    };
  }

  handleResetCounter() {
    console.log(this.n2oRef);
    this.count = 0;
    this.n2oRef.current.store.dispatch(
      setCounter("testCounterId", this.count)
    );
  }

  render() {
    return (
      <div>
        <div style={{ padding: "1rem 2rem 0rem 2rem" }}>
          <h4>
            Demo: {pkg.name} {pkg.version}
          </h4>
          <ButtonToolbar>
            <ButtonGroup>
              <Button onClick={this.handleAddNotify}>Add</Button>
              <Button onClick={this.handleClearAllNotify}>Clear all</Button>
            </ButtonGroup>
            {'\u2000'}
            <ButtonGroup>
              <Button onClick={this.handleIncreaseCounter(1)}>
                Counter +1
              </Button>
              <Button onClick={this.handleIncreaseCounter(10)}>
                Counter +10
              </Button>
              <Button onClick={this.handleIncreaseCounter(100)}>
                Counter +100
              </Button>
              <Button onClick={this.handleResetCounter}>
                Reset
              </Button>
            </ButtonGroup>
          </ButtonToolbar>
        </div>
        <hr />
        <N2O ref={this.n2oRef} {...config} />
      </div>
    );
  }
}

render(<Demo />, document.querySelector("#demo"));
