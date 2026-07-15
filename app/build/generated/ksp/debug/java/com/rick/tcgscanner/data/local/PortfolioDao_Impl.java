package com.rick.tcgscanner.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PortfolioDao_Impl implements PortfolioDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PortfolioEntity> __insertionAdapterOfPortfolioEntity;

  private final EntityDeletionOrUpdateAdapter<PortfolioEntity> __deletionAdapterOfPortfolioEntity;

  private final EntityDeletionOrUpdateAdapter<PortfolioEntity> __updateAdapterOfPortfolioEntity;

  public PortfolioDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPortfolioEntity = new EntityInsertionAdapter<PortfolioEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `portfolio` (`id`,`cardId`,`name`,`setName`,`number`,`rarity`,`imageUrl`,`smallImageUrl`,`quantity`,`condition`,`purchasePrice`,`notes`,`ungradedMarket`,`psa9`,`psa10`,`cgc95`,`cgc10`,`bgs95`,`addedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PortfolioEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCardId());
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getSetName());
        statement.bindString(5, entity.getNumber());
        statement.bindString(6, entity.getRarity());
        statement.bindString(7, entity.getImageUrl());
        statement.bindString(8, entity.getSmallImageUrl());
        statement.bindLong(9, entity.getQuantity());
        statement.bindString(10, entity.getCondition());
        statement.bindDouble(11, entity.getPurchasePrice());
        statement.bindString(12, entity.getNotes());
        statement.bindDouble(13, entity.getUngradedMarket());
        statement.bindDouble(14, entity.getPsa9());
        statement.bindDouble(15, entity.getPsa10());
        statement.bindDouble(16, entity.getCgc95());
        statement.bindDouble(17, entity.getCgc10());
        statement.bindDouble(18, entity.getBgs95());
        statement.bindLong(19, entity.getAddedAt());
      }
    };
    this.__deletionAdapterOfPortfolioEntity = new EntityDeletionOrUpdateAdapter<PortfolioEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `portfolio` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PortfolioEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPortfolioEntity = new EntityDeletionOrUpdateAdapter<PortfolioEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `portfolio` SET `id` = ?,`cardId` = ?,`name` = ?,`setName` = ?,`number` = ?,`rarity` = ?,`imageUrl` = ?,`smallImageUrl` = ?,`quantity` = ?,`condition` = ?,`purchasePrice` = ?,`notes` = ?,`ungradedMarket` = ?,`psa9` = ?,`psa10` = ?,`cgc95` = ?,`cgc10` = ?,`bgs95` = ?,`addedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PortfolioEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCardId());
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getSetName());
        statement.bindString(5, entity.getNumber());
        statement.bindString(6, entity.getRarity());
        statement.bindString(7, entity.getImageUrl());
        statement.bindString(8, entity.getSmallImageUrl());
        statement.bindLong(9, entity.getQuantity());
        statement.bindString(10, entity.getCondition());
        statement.bindDouble(11, entity.getPurchasePrice());
        statement.bindString(12, entity.getNotes());
        statement.bindDouble(13, entity.getUngradedMarket());
        statement.bindDouble(14, entity.getPsa9());
        statement.bindDouble(15, entity.getPsa10());
        statement.bindDouble(16, entity.getCgc95());
        statement.bindDouble(17, entity.getCgc10());
        statement.bindDouble(18, entity.getBgs95());
        statement.bindLong(19, entity.getAddedAt());
        statement.bindLong(20, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final PortfolioEntity entity, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPortfolioEntity.insertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final PortfolioEntity entity, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPortfolioEntity.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final PortfolioEntity entity, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPortfolioEntity.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PortfolioEntity>> getAll() {
    final String _sql = "SELECT * FROM portfolio ORDER BY addedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"portfolio"}, new Callable<List<PortfolioEntity>>() {
      @Override
      @NonNull
      public List<PortfolioEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "cardId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSetName = CursorUtil.getColumnIndexOrThrow(_cursor, "setName");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfRarity = CursorUtil.getColumnIndexOrThrow(_cursor, "rarity");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfSmallImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "smallImageUrl");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfCondition = CursorUtil.getColumnIndexOrThrow(_cursor, "condition");
          final int _cursorIndexOfPurchasePrice = CursorUtil.getColumnIndexOrThrow(_cursor, "purchasePrice");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfUngradedMarket = CursorUtil.getColumnIndexOrThrow(_cursor, "ungradedMarket");
          final int _cursorIndexOfPsa9 = CursorUtil.getColumnIndexOrThrow(_cursor, "psa9");
          final int _cursorIndexOfPsa10 = CursorUtil.getColumnIndexOrThrow(_cursor, "psa10");
          final int _cursorIndexOfCgc95 = CursorUtil.getColumnIndexOrThrow(_cursor, "cgc95");
          final int _cursorIndexOfCgc10 = CursorUtil.getColumnIndexOrThrow(_cursor, "cgc10");
          final int _cursorIndexOfBgs95 = CursorUtil.getColumnIndexOrThrow(_cursor, "bgs95");
          final int _cursorIndexOfAddedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "addedAt");
          final List<PortfolioEntity> _result = new ArrayList<PortfolioEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PortfolioEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpCardId;
            _tmpCardId = _cursor.getString(_cursorIndexOfCardId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSetName;
            _tmpSetName = _cursor.getString(_cursorIndexOfSetName);
            final String _tmpNumber;
            _tmpNumber = _cursor.getString(_cursorIndexOfNumber);
            final String _tmpRarity;
            _tmpRarity = _cursor.getString(_cursorIndexOfRarity);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpSmallImageUrl;
            _tmpSmallImageUrl = _cursor.getString(_cursorIndexOfSmallImageUrl);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final String _tmpCondition;
            _tmpCondition = _cursor.getString(_cursorIndexOfCondition);
            final float _tmpPurchasePrice;
            _tmpPurchasePrice = _cursor.getFloat(_cursorIndexOfPurchasePrice);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final float _tmpUngradedMarket;
            _tmpUngradedMarket = _cursor.getFloat(_cursorIndexOfUngradedMarket);
            final float _tmpPsa9;
            _tmpPsa9 = _cursor.getFloat(_cursorIndexOfPsa9);
            final float _tmpPsa10;
            _tmpPsa10 = _cursor.getFloat(_cursorIndexOfPsa10);
            final float _tmpCgc95;
            _tmpCgc95 = _cursor.getFloat(_cursorIndexOfCgc95);
            final float _tmpCgc10;
            _tmpCgc10 = _cursor.getFloat(_cursorIndexOfCgc10);
            final float _tmpBgs95;
            _tmpBgs95 = _cursor.getFloat(_cursorIndexOfBgs95);
            final long _tmpAddedAt;
            _tmpAddedAt = _cursor.getLong(_cursorIndexOfAddedAt);
            _item = new PortfolioEntity(_tmpId,_tmpCardId,_tmpName,_tmpSetName,_tmpNumber,_tmpRarity,_tmpImageUrl,_tmpSmallImageUrl,_tmpQuantity,_tmpCondition,_tmpPurchasePrice,_tmpNotes,_tmpUngradedMarket,_tmpPsa9,_tmpPsa10,_tmpCgc95,_tmpCgc10,_tmpBgs95,_tmpAddedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getById(final int id, final Continuation<? super PortfolioEntity> $completion) {
    final String _sql = "SELECT * FROM portfolio WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PortfolioEntity>() {
      @Override
      @Nullable
      public PortfolioEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "cardId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSetName = CursorUtil.getColumnIndexOrThrow(_cursor, "setName");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfRarity = CursorUtil.getColumnIndexOrThrow(_cursor, "rarity");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfSmallImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "smallImageUrl");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfCondition = CursorUtil.getColumnIndexOrThrow(_cursor, "condition");
          final int _cursorIndexOfPurchasePrice = CursorUtil.getColumnIndexOrThrow(_cursor, "purchasePrice");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfUngradedMarket = CursorUtil.getColumnIndexOrThrow(_cursor, "ungradedMarket");
          final int _cursorIndexOfPsa9 = CursorUtil.getColumnIndexOrThrow(_cursor, "psa9");
          final int _cursorIndexOfPsa10 = CursorUtil.getColumnIndexOrThrow(_cursor, "psa10");
          final int _cursorIndexOfCgc95 = CursorUtil.getColumnIndexOrThrow(_cursor, "cgc95");
          final int _cursorIndexOfCgc10 = CursorUtil.getColumnIndexOrThrow(_cursor, "cgc10");
          final int _cursorIndexOfBgs95 = CursorUtil.getColumnIndexOrThrow(_cursor, "bgs95");
          final int _cursorIndexOfAddedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "addedAt");
          final PortfolioEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpCardId;
            _tmpCardId = _cursor.getString(_cursorIndexOfCardId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSetName;
            _tmpSetName = _cursor.getString(_cursorIndexOfSetName);
            final String _tmpNumber;
            _tmpNumber = _cursor.getString(_cursorIndexOfNumber);
            final String _tmpRarity;
            _tmpRarity = _cursor.getString(_cursorIndexOfRarity);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpSmallImageUrl;
            _tmpSmallImageUrl = _cursor.getString(_cursorIndexOfSmallImageUrl);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final String _tmpCondition;
            _tmpCondition = _cursor.getString(_cursorIndexOfCondition);
            final float _tmpPurchasePrice;
            _tmpPurchasePrice = _cursor.getFloat(_cursorIndexOfPurchasePrice);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final float _tmpUngradedMarket;
            _tmpUngradedMarket = _cursor.getFloat(_cursorIndexOfUngradedMarket);
            final float _tmpPsa9;
            _tmpPsa9 = _cursor.getFloat(_cursorIndexOfPsa9);
            final float _tmpPsa10;
            _tmpPsa10 = _cursor.getFloat(_cursorIndexOfPsa10);
            final float _tmpCgc95;
            _tmpCgc95 = _cursor.getFloat(_cursorIndexOfCgc95);
            final float _tmpCgc10;
            _tmpCgc10 = _cursor.getFloat(_cursorIndexOfCgc10);
            final float _tmpBgs95;
            _tmpBgs95 = _cursor.getFloat(_cursorIndexOfBgs95);
            final long _tmpAddedAt;
            _tmpAddedAt = _cursor.getLong(_cursorIndexOfAddedAt);
            _result = new PortfolioEntity(_tmpId,_tmpCardId,_tmpName,_tmpSetName,_tmpNumber,_tmpRarity,_tmpImageUrl,_tmpSmallImageUrl,_tmpQuantity,_tmpCondition,_tmpPurchasePrice,_tmpNotes,_tmpUngradedMarket,_tmpPsa9,_tmpPsa10,_tmpCgc95,_tmpCgc10,_tmpBgs95,_tmpAddedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
