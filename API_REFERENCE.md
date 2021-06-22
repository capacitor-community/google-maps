# API Reference 🔌

Below is an index of all the methods available.

<docgen-index>

- [`initialize(...)`](#initialize)
- [`create(...)`](#create)
- [`elementFromPointResult(...)`](#elementfrompointresult)
- [`addMarker(...)`](#addmarker)
- [`didCloseInfoWindow(...)`](#didcloseinfowindow)
- [`didTapMap(...)`](#didtapmap)
- [`didTapMarker(...)`](#didtapmarker)
- [`addListener('didRequestElementFromPoint', ...)`](#addlistenerdidrequestelementfrompoint-)
- [Interfaces](#interfaces)
- [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### initialize(...)

```typescript
initialize(options: InitializeOptions) => Promise<void>
```

| Param         | Type                                                            |
| ------------- | --------------------------------------------------------------- |
| **`options`** | <code><a href="#initializeoptions">InitializeOptions</a></code> |

---

### create(...)

```typescript
create(options?: CreateOptions) => Promise<CreateResult>
```

Creates map view and displays it

| Param         | Type                                                    |
| ------------- | ------------------------------------------------------- |
| **`options`** | <code><a href="#createoptions">CreateOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#createresult">CreateResult</a>&gt;</code>

---

### elementFromPointResult(...)

```typescript
elementFromPointResult(options: ElementFromPointResultOptions) => Promise<void>
```

| Param         | Type                                                                                    |
| ------------- | --------------------------------------------------------------------------------------- |
| **`options`** | <code><a href="#elementfrompointresultoptions">ElementFromPointResultOptions</a></code> |

---

### addMarker(...)

```typescript
addMarker(options: AddMarkerOptions) => Promise<MarkerAndPositionResult>
```

Adds a marker on the map

| Param         | Type                                                          |
| ------------- | ------------------------------------------------------------- |
| **`options`** | <code><a href="#addmarkeroptions">AddMarkerOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#markerandpositionresult">MarkerAndPositionResult</a>&gt;</code>

---

### didCloseInfoWindow(...)

```typescript
didCloseInfoWindow(options: DefaultEventOptions, callback: DidCloseInfoWindowCallback) => Promise<CallbackID>
```

| Param          | Type                                                                              |
| -------------- | --------------------------------------------------------------------------------- |
| **`options`**  | <code><a href="#defaulteventoptions">DefaultEventOptions</a></code>               |
| **`callback`** | <code><a href="#didcloseinfowindowcallback">DidCloseInfoWindowCallback</a></code> |

**Returns:** <code>Promise&lt;string&gt;</code>

---

### didTapMap(...)

```typescript
didTapMap(options: DefaultEventOptions, callback: DidTapMapCallback) => Promise<CallbackID>
```

| Param          | Type                                                                |
| -------------- | ------------------------------------------------------------------- |
| **`options`**  | <code><a href="#defaulteventoptions">DefaultEventOptions</a></code> |
| **`callback`** | <code><a href="#didtapmapcallback">DidTapMapCallback</a></code>     |

**Returns:** <code>Promise&lt;string&gt;</code>

---

### didTapMarker(...)

```typescript
didTapMarker(options: DefaultEventWithPreventDefaultOptions, callback: DidTapMarkerCallback) => Promise<CallbackID>
```

| Param          | Type                                                                                                    |
| -------------- | ------------------------------------------------------------------------------------------------------- |
| **`options`**  | <code><a href="#defaulteventwithpreventdefaultoptions">DefaultEventWithPreventDefaultOptions</a></code> |
| **`callback`** | <code><a href="#didtapmarkercallback">DidTapMarkerCallback</a></code>                                   |

**Returns:** <code>Promise&lt;string&gt;</code>

---

### addListener('didRequestElementFromPoint', ...)

```typescript
addListener(eventName: "didRequestElementFromPoint", listenerFunc: (result: DidRequestElementFromPointResult) => void) => PluginListenerHandle
```

| Param              | Type                                                                                                               |
| ------------------ | ------------------------------------------------------------------------------------------------------------------ |
| **`eventName`**    | <code>'didRequestElementFromPoint'</code>                                                                          |
| **`listenerFunc`** | <code>(result: <a href="#didrequestelementfrompointresult">DidRequestElementFromPointResult</a>) =&gt; void</code> |

**Returns:** <code><a href="#pluginlistenerhandle">PluginListenerHandle</a></code>

---

### Interfaces

#### InitializeOptions

| Prop                   | Type                |
| ---------------------- | ------------------- |
| **`devicePixelRatio`** | <code>number</code> |
| **`key`**              | <code>string</code> |

#### CreateResult

| Prop        | Type                |
| ----------- | ------------------- |
| **`mapId`** | <code>string</code> |

#### CreateOptions

| Prop            | Type                 |
| --------------- | -------------------- |
| **`latitude`**  | <code>number</code>  |
| **`longitude`** | <code>number</code>  |
| **`zoom`**      | <code>number</code>  |
| **`liteMode`**  | <code>boolean</code> |
| **`width`**     | <code>number</code>  |
| **`height`**    | <code>number</code>  |
| **`x`**         | <code>number</code>  |
| **`y`**         | <code>number</code>  |

#### ElementFromPointResultOptions

| Prop               | Type                 |
| ------------------ | -------------------- |
| **`eventChainId`** | <code>string</code>  |
| **`mapId`**        | <code>string</code>  |
| **`isSameNode`**   | <code>boolean</code> |

#### MarkerAndPositionResult

| Prop           | Type                                          |
| -------------- | --------------------------------------------- |
| **`position`** | <code><a href="#position">Position</a></code> |
| **`marker`**   | <code><a href="#marker">Marker</a></code>     |

#### Position

| Prop            | Type                |
| --------------- | ------------------- |
| **`latitude`**  | <code>number</code> |
| **`longitude`** | <code>number</code> |

#### Marker

| Prop              | Type                 |
| ----------------- | -------------------- |
| **`id`**          | <code>string</code>  |
| **`title`**       | <code>string</code>  |
| **`snippet`**     | <code>string</code>  |
| **`opacity`**     | <code>number</code>  |
| **`isFlat`**      | <code>boolean</code> |
| **`isDraggable`** | <code>boolean</code> |
| **`metadata`**    | <code>object</code>  |

#### AddMarkerOptions

| Prop              | Type                 |
| ----------------- | -------------------- |
| **`mapId`**       | <code>string</code>  |
| **`latitude`**    | <code>number</code>  |
| **`longitude`**   | <code>number</code>  |
| **`title`**       | <code>string</code>  |
| **`snippet`**     | <code>string</code>  |
| **`opacity`**     | <code>number</code>  |
| **`isFlat`**      | <code>boolean</code> |
| **`isDraggable`** | <code>boolean</code> |
| **`metadata`**    | <code>object</code>  |

#### DefaultEventOptions

| Prop        | Type                |
| ----------- | ------------------- |
| **`mapId`** | <code>string</code> |

#### PositionResult

| Prop           | Type                                          |
| -------------- | --------------------------------------------- |
| **`position`** | <code><a href="#position">Position</a></code> |

#### DefaultEventWithPreventDefaultOptions

| Prop                 | Type                 |
| -------------------- | -------------------- |
| **`mapId`**          | <code>string</code>  |
| **`preventDefault`** | <code>boolean</code> |

#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |

#### DidRequestElementFromPointResult

| Prop               | Type                                   |
| ------------------ | -------------------------------------- |
| **`eventChainId`** | <code>string</code>                    |
| **`point`**        | <code>{ x: number; y: number; }</code> |

### Type Aliases

#### DidCloseInfoWindowCallback

<code>(message: <a href="#markerandpositionresult">MarkerAndPositionResult</a>, err?: any): void</code>

#### CallbackID

<code>string</code>

#### DidTapMapCallback

<code>(message: <a href="#positionresult">PositionResult</a>, err?: any): void</code>

#### DidTapMarkerCallback

<code>(message: <a href="#markerandpositionresult">MarkerAndPositionResult</a>, err?: any): void</code>

</docgen-api>
