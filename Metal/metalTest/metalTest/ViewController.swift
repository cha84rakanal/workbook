//
//  ViewController.swift
//  metalTest
//
//  Created by Tatsuya Minagawa on 2019/01/04.
//  Copyright © 2019年 Tatsuya Minagawa. All rights reserved.
//

import UIKit
import MetalKit

/* 頂点と色の情報 */
struct Vertex {
    var position: vector_float4
    var color: vector_float4
}

class ViewController: UIViewController, MTKViewDelegate {
    
    //https://qiita.com/clockmaker/items/a5be47a802e602a894ac
    
    private let device = MTLCreateSystemDefaultDevice()!
    private var commandQueue: MTLCommandQueue!
    private var renderPassDescriptor: MTLRenderPassDescriptor!
    private var texture: MTLTexture!
    
    @IBOutlet private weak var mtkView: MTKView!
    
    private let positionData: [Float] = [
        +0.00, +0.75, 0, +1,
        +0.75, -0.75, 0, +1,
        -0.75, -0.75, 0, +1
    ]
    
    private let colorData: [Float] = [
        1, 1, 1, 1,
        0, 1, 0, 1,
        0, 1, 1, 1,
    ]
    
    private var bufferPosition: MTLBuffer!
    private var bufferColor: MTLBuffer!
    private var renderPipelineState: MTLRenderPipelineState!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Metalのセットアップ
        setupMetal()
        
        // バッファーを作成
        makeBuffers()
        // パイプラインを作成
        makePipeline()
        
        mtkView.enableSetNeedsDisplay = true
        // ビューの更新依頼 → draw(in:)が呼ばれる
        mtkView.setNeedsDisplay()
    }
    
    private func setupMetal() {
        // MTLCommandQueueを初期化
        commandQueue = device.makeCommandQueue()
        
        renderPassDescriptor = MTLRenderPassDescriptor()
        // このRender Passが実行されるときの挙動を設定
        renderPassDescriptor.colorAttachments[0].loadAction = MTLLoadAction.clear
        renderPassDescriptor.colorAttachments[0].storeAction = MTLStoreAction.store
        // 背景色は黒にする
        renderPassDescriptor.colorAttachments[0].clearColor = MTLClearColorMake(0.0, 0.0, 0.0, 1.0)
        
        // MTKViewのセットアップ
        mtkView.device = device
        mtkView.delegate = self
    }
    
    private func makeBuffers() {
        let size = positionData.count * MemoryLayout<Float>.size
        // 位置情報のバッファーを作成
        bufferPosition = device.makeBuffer(bytes: positionData, length: size)
        // 色情報のバッファーを作成
        bufferColor = device.makeBuffer(bytes: colorData, length: size)
    }
    
    private func makePipeline() {
        guard let library = device.makeDefaultLibrary() else {fatalError()}
        let descriptor = MTLRenderPipelineDescriptor()
        descriptor.vertexFunction = library.makeFunction(name: "myVertexShader")
        descriptor.fragmentFunction = library.makeFunction(name: "myFragmentShader")
        descriptor.colorAttachments[0].pixelFormat = .bgra8Unorm
        // レンダーパイプラインステートを作成
        renderPipelineState = try! device.makeRenderPipelineState(descriptor: descriptor)
    }
    
    // MARK: - MTKViewDelegate
    
    func mtkView(_ view: MTKView, drawableSizeWillChange size: CGSize) {
        print("\(self.classForCoder)/" + #function)
    }
    
    func draw(in view: MTKView) {
        // ドローアブルを取得
        guard let drawable = view.currentDrawable else {fatalError()}
        renderPassDescriptor.colorAttachments[0].texture = drawable.texture
        // コマンドバッファを作成
        guard let cBuffer = commandQueue.makeCommandBuffer() else {fatalError()}
        // エンコーダ生成
        let encoder = cBuffer.makeRenderCommandEncoder(
            descriptor: renderPassDescriptor
            )!
        encoder.setRenderPipelineState(renderPipelineState)
        // バッファーを頂点シェーダーに送る
        encoder.setVertexBuffer(bufferPosition, offset: 0, index: 0)
        encoder.setVertexBuffer(bufferColor, offset: 0, index:1)
        // 三角形を作成
        encoder.drawPrimitives(type: MTLPrimitiveType.triangle,
                               vertexStart: 0,
                               vertexCount: 3)
        // エンコード完了
        encoder.endEncoding()
        // 表示するドローアブルを登録
        cBuffer.present(drawable)
        // コマンドバッファをコミット（エンキュー）
        cBuffer.commit()
        print("commited")
    }
}

